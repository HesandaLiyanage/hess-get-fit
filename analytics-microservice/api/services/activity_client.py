import httpx
from typing import List, Optional
from datetime import datetime
import py_eureka_client.eureka_client as eureka_client
import logging

from api.models import Activity

logger = logging.getLogger(__name__)


class ActivityClient:
    """Client to fetch activities from Activity Service via Eureka"""

    def __init__(self):
        self.service_name = "activity-service"  # Must match Eureka registration

    async def get_user_activities(self, user_id: str) -> List[Activity]:
        """Fetch all activities for a user from Activity Service"""
        try:
            # Get service URL from Eureka
            service_url = await eureka_client.get_service_url(self.service_name)

            async with httpx.AsyncClient() as client:
                response = await client.get(
                    f"{service_url}/api/activities",
                    headers={"X-User-ID": user_id},
                    timeout=10.0
                )
                response.raise_for_status()

                activities_data = response.json()
                return [Activity(**activity) for activity in activities_data]

        except Exception as e:
            logger.error(f"Error fetching activities for user {user_id}: {e}")
            return []

    async def get_activities_since(self, user_id: str, since: datetime) -> List[Activity]:
        """Fetch activities since a specific date"""
        # For now, filter client-side (Activity Service could add date query params)
        all_activities = await self.get_user_activities(user_id)
        return [a for a in all_activities if a.startTime >= since]