from fastapi import APIRouter

router  = APIRouter()

@router.get("/")
def read_events():
    #this shows ther
    return {
        "items": [1,2,3]
    }

@router.get("/analytics/users/{userId}/summary")
def user_summery(userId):
    return {
        "analysis": [1,2,3]
    }

@router.get("/analytics/users/{userId}/stats?period=weekly")
def user_weekly_stats(userId):
    return {
        "analysis": [1,2,3]
    }

@router.get("/analytics/users/{userId}/stats?period=daily")
def user_daily_stats(userId):
    return {
        "analysis": [1,2,3]
    }

@router.get("/analytics/users/{userId}/streaks")
def user_streaks(userId):
    return {
        "analysis": [1,2,3]
    }