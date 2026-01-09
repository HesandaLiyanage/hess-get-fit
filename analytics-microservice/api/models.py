from pydantic import BaseModel
from typing import Optional, List, Dict
from datetime import datetime, date
from enum import Enum


# ========== Enums ==========

class ActivityType(str, Enum):
    RUNNING = "RUNNING"
    WALKING = "WALKING"
    CYCLING = "CYCLING"
    GYM = "GYM"
    YOGA = "YOGA"
    SWIMMING = "SWIMMING"
    CARDIO = "CARDIO"
    HIIT = "HIIT"
    OTHER = "OTHER"


class Period(str, Enum):
    DAILY = "daily"
    WEEKLY = "weekly"
    MONTHLY = "monthly"


# ========== Activity from Activity-Service ==========

class Activity(BaseModel):
    id: str
    userId: str
    type: ActivityType
    duration: int  # minutes
    calories: int
    startTime: datetime
    additionalInfo: Optional[Dict] = None
    createdAt: datetime
    updatedAt: datetime


# ========== Response Models ==========

class UserSummaryResponse(BaseModel):
    """Response for /users/{userId}/summary"""
    userId: str
    totalActivities: int
    totalDuration: int  # total minutes
    totalCalories: int
    averageDuration: float
    averageCalories: float
    favoriteActivityType: Optional[str]  # most frequent type
    firstActivityDate: Optional[datetime]
    lastActivityDate: Optional[datetime]


class PeriodStatsResponse(BaseModel):
    """Response for /users/{userId}/stats"""
    userId: str
    period: str  # "daily" or "weekly"
    periodStart: date
    periodEnd: date
    totalActivities: int
    totalDuration: int
    totalCalories: int
    averageDurationPerDay: float
    averageCaloriesPerDay: float
    activeDays: int  # days with at least 1 activity


class StreakResponse(BaseModel):
    """Response for /users/{userId}/streaks"""
    userId: str
    currentStreak: int  # consecutive days with activity (ending today or yesterday)
    longestStreak: int  # best ever
    activeDaysThisWeek: int  # out of 7
    activeDaysThisMonth: int  # out of current month days
    lastActivityDate: Optional[date]