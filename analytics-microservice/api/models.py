from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime

class AnalyticsRequest(BaseModel):
    user_id: Optional[str] = None
    first_name: str
    last_name: str

class AnalyticsResponse(BaseModel):
    user_id: str
    first_name: str
    last_name: str

class HealthResponse(BaseModel):
    user_id: str
    first_name: str
    last_name: str


