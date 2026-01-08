from datetime import datetime
import uuid
from typing import Dict, Any


class AnalyticsService:

    def __init__(self):
        self.data_store = []

    def process_analytics(self, event_type: str, data: Dict[str, Any]) -> Dict[str, Any]:
        """Process analytics data"""
        analytics_record = {
            "id": str(uuid.uuid4()),
            "timestamp": datetime.now().isoformat(),
            "event_type": event_type,
            "data": data,
            "processed": True
        }

        self.data_store.append(analytics_record)

        return {
            "message": "Analytics processed successfully",
            "record_id": analytics_record["id"],
            "total_records": len(self.data_store)
        }

    def get_analytics_summary(self) -> Dict[str, Any]:
        """Get analytics summary"""
        return {
            "total_events": len(self.data_store),
            "last_updated": datetime.now().isoformat(),
            "event_types": list(set(record["event_type"] for record in self.data_store))
        }

    def get_all_analytics(self) -> list:
        """Get all analytics records"""
        return self.data_store