from typing import List, Optional, Dict
from datetime import datetime, date, timedelta
from collections import Counter

from api.models import (
    Activity,
    UserSummaryResponse,
    PeriodStatsResponse,
    StreakResponse
)
from api.services.activity_client import ActivityClient


class AnalyticsService:

    def __init__(self):
        self.activity_client = ActivityClient()

    # ===================================================
    # ENDPOINT 1: /users/{userId}/summary
    # ===================================================
    async def get_user_summary(self, user_id: str) -> UserSummaryResponse:
        """
        Calculate all-time summary stats for a user.
        """
        activities = await self.activity_client.get_user_activities(user_id)

        if not activities:
            return UserSummaryResponse(
                userId=user_id,
                totalActivities=0,
                totalDuration=0,
                totalCalories=0,
                averageDuration=0.0,
                averageCalories=0.0,
                favoriteActivityType=None,
                firstActivityDate=None,
                lastActivityDate=None
            )

        # Calculate totals
        total_duration = sum(a.duration for a in activities)
        total_calories = sum(a.calories for a in activities)
        total_count = len(activities)

        # Calculate averages
        avg_duration = total_duration / total_count
        avg_calories = total_calories / total_count

        # Find favorite activity type (most frequent)
        type_counts = Counter(a.type for a in activities)
        favorite_type = type_counts.most_common(1)[0][0].value if type_counts else None

        # Find date range
        sorted_activities = sorted(activities, key=lambda a: a.startTime)
        first_date = sorted_activities[0].startTime
        last_date = sorted_activities[-1].startTime

        return UserSummaryResponse(
            userId=user_id,
            totalActivities=total_count,
            totalDuration=total_duration,
            totalCalories=total_calories,
            averageDuration=round(avg_duration, 2),
            averageCalories=round(avg_calories, 2),
            favoriteActivityType=favorite_type,
            firstActivityDate=first_date,
            lastActivityDate=last_date
        )

    # ===================================================
    # ENDPOINT 2: /users/{userId}/stats?period=weekly|daily
    # ===================================================
    async def get_period_stats(self, user_id: str, period: str) -> PeriodStatsResponse:
        """
        Calculate stats for a specific period (daily or weekly).

        - daily: today's stats
        - weekly: last 7 days stats
        """
        today = date.today()

        if period == "daily":
            period_start = today
            period_end = today
            num_days = 1
        elif period == "weekly":
            period_start = today - timedelta(days=6)  # Last 7 days including today
            period_end = today
            num_days = 7
        else:
            # Default to weekly
            period_start = today - timedelta(days=6)
            period_end = today
            num_days = 7

        # Fetch activities since period start
        since_datetime = datetime.combine(period_start, datetime.min.time())
        activities = await self.activity_client.get_activities_since(user_id, since_datetime)

        # Filter to only activities within the period
        period_activities = [
            a for a in activities
            if period_start <= a.startTime.date() <= period_end
        ]

        if not period_activities:
            return PeriodStatsResponse(
                userId=user_id,
                period=period,
                periodStart=period_start,
                periodEnd=period_end,
                totalActivities=0,
                totalDuration=0,
                totalCalories=0,
                averageDurationPerDay=0.0,
                averageCaloriesPerDay=0.0,
                activeDays=0
            )

        # Calculate totals
        total_duration = sum(a.duration for a in period_activities)
        total_calories = sum(a.calories for a in period_activities)
        total_count = len(period_activities)

        # Calculate active days (unique dates with at least 1 activity)
        active_dates = set(a.startTime.date() for a in period_activities)
        active_days = len(active_dates)

        # Calculate daily averages
        avg_duration_per_day = total_duration / num_days
        avg_calories_per_day = total_calories / num_days

        return PeriodStatsResponse(
            userId=user_id,
            period=period,
            periodStart=period_start,
            periodEnd=period_end,
            totalActivities=total_count,
            totalDuration=total_duration,
            totalCalories=total_calories,
            averageDurationPerDay=round(avg_duration_per_day, 2),
            averageCaloriesPerDay=round(avg_calories_per_day, 2),
            activeDays=active_days
        )

    # ===================================================
    # ENDPOINT 3: /users/{userId}/streaks
    # ===================================================
    async def get_user_streaks(self, user_id: str) -> StreakResponse:
        """
        Calculate workout streaks and consistency metrics.

        - Current streak: consecutive days ending today or yesterday
        - Longest streak: best ever consecutive days
        - Active days this week/month
        """
        activities = await self.activity_client.get_user_activities(user_id)

        if not activities:
            return StreakResponse(
                userId=user_id,
                currentStreak=0,
                longestStreak=0,
                activeDaysThisWeek=0,
                activeDaysThisMonth=0,
                lastActivityDate=None
            )

        # Get unique active dates (sorted descending)
        active_dates = sorted(
            set(a.startTime.date() for a in activities),
            reverse=True
        )

        today = date.today()
        yesterday = today - timedelta(days=1)

        # ---- Calculate Current Streak ----
        current_streak = 0

        # Check if streak is still active (last activity today or yesterday)
        if active_dates[0] >= yesterday:
            # Start counting from most recent activity
            check_date = active_dates[0]

            for active_date in active_dates:
                if active_date == check_date:
                    current_streak += 1
                    check_date -= timedelta(days=1)
                elif active_date < check_date:
                    # Gap found, streak ends
                    break

        # ---- Calculate Longest Streak ----
        longest_streak = 0
        temp_streak = 1

        sorted_dates_asc = sorted(active_dates)
        for i in range(1, len(sorted_dates_asc)):
            if sorted_dates_asc[i] - sorted_dates_asc[i - 1] == timedelta(days=1):
                temp_streak += 1
            else:
                longest_streak = max(longest_streak, temp_streak)
                temp_streak = 1
        longest_streak = max(longest_streak, temp_streak)

        # ---- Active Days This Week ----
        week_start = today - timedelta(days=today.weekday())  # Monday
        active_this_week = sum(
            1 for d in active_dates
            if week_start <= d <= today
        )

        # ---- Active Days This Month ----
        month_start = today.replace(day=1)
        active_this_month = sum(
            1 for d in active_dates
            if month_start <= d <= today
        )

        return StreakResponse(
            userId=user_id,
            currentStreak=current_streak,
            longestStreak=longest_streak,
            activeDaysThisWeek=active_this_week,
            activeDaysThisMonth=active_this_month,
            lastActivityDate=active_dates[0] if active_dates else None
        )