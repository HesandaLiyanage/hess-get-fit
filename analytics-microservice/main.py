from fastapi import FastAPI, HTTPException, Query
from api.models import UserSummaryResponse, PeriodStatsResponse, StreakResponse, Period
from api.services.analytics_service import AnalyticsService

app = FastAPI(title="Analytics Service")
analytics_service = AnalyticsService()


# ============================================
# ENDPOINT 1: User Summary
# ============================================
@app.get("/analytics/users/{user_id}/summary", response_model=UserSummaryResponse)
async def get_user_summary(user_id: str):
    """
    Get all-time summary statistics for a user.

    Returns:
    - Total activities, duration, calories
    - Average duration and calories per activity
    - Favorite activity type
    - First and last activity dates
    """
    try:
        return await analytics_service.get_user_summary(user_id)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# ============================================
# ENDPOINT 2: Period Stats (Daily/Weekly)
# ============================================
@app.get("/analytics/users/{user_id}/stats", response_model=PeriodStatsResponse)
async def get_period_stats(
        user_id: str,
        period: Period = Query(Period.WEEKLY, description="Period: 'daily' or 'weekly'")
):
    """
    Get statistics for a specific period.

    - daily: Today's stats only
    - weekly: Last 7 days stats

    Returns:
    - Total activities, duration, calories for the period
    - Average per day
    - Number of active days
    """
    try:
        return await analytics_service.get_period_stats(user_id, period.value)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# ============================================
# ENDPOINT 3: Streaks
# ============================================
@app.get("/analytics/users/{user_id}/streaks", response_model=StreakResponse)
async def get_user_streaks(user_id: str):
    """
    Get workout streak and consistency statistics.

    Returns:
    - Current streak (consecutive days up to today/yesterday)
    - Longest streak ever
    - Active days this week (Mon-today)
    - Active days this month (1st-today)
    """
    try:
        return await analytics_service.get_user_streaks(user_id)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))