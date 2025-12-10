# AIEnglishSpeaking

AI English Speaking — a Java webapp for practicing English speaking with AI features.

## Overview

- Java web application (WAR) using Servlets and JSP
- Target Java: 21

## Quick start (local)

1. Install Java 21 and Maven
2. Set `JAVA_HOME` to your JDK 21 installation
3. Build:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
mvn -U clean package
```

## Repository

This project was prepared for upgrade to Java 21. See `pom.xml` for compiler settings.

## License

This repository uses the MIT License (see `LICENSE`).
