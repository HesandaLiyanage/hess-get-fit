from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    app_name: str = "analytics-service"
    app_port: int = 8000
    eureka_server: str = "http://localhost:8761/eureka"
    instance_host: str = "localhost"

    class Config:
        env_file = ".env"

settings = Settings()