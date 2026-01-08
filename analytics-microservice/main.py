from fastapi import FastAPI
import py_eureka_client.eureka_client as eureka_client
from app.config import settings

app = FastAPI()

@app.get("/")
async def root():
    return {"message": "Analytics Service"}

@app.get("/health")
async def health():
    return {"status": "UP"}

@app.on_event("startup")
async def startup_event():
    await eureka_client.init_async(
        eureka_server=settings.eureka_server,
        app_name=settings.app_name,
        instance_port=settings.app_port,
        instance_host=settings.instance_host,
        health_check_url="/health",
        status_page_url="/health"
    )

@app.on_event("shutdown")
async def shutdown_event():
    await eureka_client.stop_async()


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=settings.app_port)