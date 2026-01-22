# PowerShell script to initialize MySQL database
# This script reads the schema.sql and executes it via MySQL

$mysqlPath = "C:\Program Files\MySQL\MySQL Server 9.5\bin\mysql.exe"
$schemaFile = "e:\Code\pp\backend\sql\schema.sql"

# Read the schema file
$schemaContent = Get-Content -Path $schemaFile -Raw

# Execute via MySQL
$schemaContent | & $mysqlPath -u root

if ($LASTEXITCODE -eq 0) {
    Write-Host "Database initialized successfully!" -ForegroundColor Green
} else {
    Write-Host "Database initialization failed with exit code $LASTEXITCODE" -ForegroundColor Red
}
