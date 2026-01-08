from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import py_eureka_client.eureka_client as eureka_client
from datetime import datetime
import logging

from app.config import settings
from app.models import AnalyticsRequest, AnalyticsResponse, HealthResponse
from app.services.analytics_service import AnalyticsService

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize FastAPI app
app = FastAPI(
    title="Analytics Service",
    description="Analytics microservice for data processing",
    version="1.0.0"
)

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Initialize analytics service
analytics_service = AnalyticsService()


@app.on_event("startup")
async def startup_event():
    """Register with Eureka on startup"""
    try:
        await eureka_client.init_async(
            eureka_server=settings.eureka_server,
            app_name=settings.app_name,
            instance_port=settings.app_port,
            instance_host=settings.instance_host,
            health_check_url=f"http://{settings.instance_host}:{settings.app_port}/health",
            status_page_url=f"http://{settings.instance_host}:{settings.app_port}/info",
            home_page_url=f"http://{settings.instance_host}:{settings.app_port}/",
            renewal_interval_in_secs=30,
            duration_in_secs=90,
        )
        logger.info(f"Successfully registered with Eureka at {settings.eureka_server}")
    except Exception as e:
        logger.error(f"Failed to register with Eureka: {str(e)}")


@app.on_event("shutdown")
async def shutdown_event():
    """Deregister from Eureka on shutdown"""
    try:
        await eureka_client.stop_async()
        logger.info("Successfully deregistered from Eureka")
    except Exception as e:
        logger.error(f"Error during Eureka deregistration: {str(e)}")


@app.get("/")
async def root():
    """Root endpoint"""
    return {
        "service": settings.app_name,
        "status": "running",
        "version": "1.0.0"
    }


@app.get("/health", response_model=HealthResponse)
async def health_check():
    """Health check endpoint for Eureka"""
    return HealthResponse(
        status="UP",
        service=settings.app_name,
        timestamp=datetime.now()
    )


@app.get("/info")
async def info():
    """Info endpoint for Eureka"""
    return {
        "app": {
            "name": settings.app_name,
            "description": "Analytics Service",
            "version": "1.0.0"
        }
    }


@app.post("/analytics/process")
async def process_analytics(request: AnalyticsRequest):
    """Process analytics data"""
    try:
        result = analytics_service.process_analytics(
            event_type=request.event_type,
            data=request.data
        )
        return result
    except Exception as e:
        logger.error(f"Error processing analytics: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/analytics/summary")
async def get_summary():
    """Get analytics summary"""
    try:
        return analytics_service.get_analytics_summary()
    except Exception as e:
        logger.error(f"Error getting summary: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/analytics/all")
async def get_all_analytics():
    """Get all analytics records"""
    try:
        return analytics_service.get_all_analytics()
    except Exception as e:
        logger.error(f"Error getting analytics: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/analytics/call-spring-service")
async def call_spring_service():
    """Example: Call another Spring Boot service via Eureka"""
    import httpx

    try:
        # Get service URL from Eureka (replace with your actual service name)
        service_url = await eureka_client.get_service_url("USER_SERVICE")

        async with httpx.AsyncClient() as client:
            response = await client.get(f"{service_url}")
            return {
                "message": "Successfully called Spring service",
                "data": response.json()
            }
    except Exception as e:
        logger.error(f"Error calling Spring service: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


if __name__ == "__main__":
    import uvicorn

    # uvicorn.run(
    #     "app.main:app",
    #     host="0.0.0.0",
    #     port=settings.app_port,
    #     reload=True
    # )
    uvicorn.run(app, host="0.0.0.0", port=settings.app_port)