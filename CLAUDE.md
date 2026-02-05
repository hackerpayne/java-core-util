# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Build Commands
- `mvn clean compile` - Clean and compile the project
- `mvn package` - Package the project into JAR files
- `mvn install` - Install artifacts to local repository
- `mvn dependency:tree > tree.txt` - Generate dependency tree

### Testing
- `mvn test` - Run unit tests
- `mvn clean test` - Clean and run tests

### Core Module Priority
The `qy-core` module must be compiled first before other modules, as it's a dependency for all other components.

## Project Architecture

### Overview
QyFramework is a comprehensive Java development framework (version 23.6.5) built on:
- **Java 21** - Latest LTS version
- **Spring Boot 3.2.0** - Modern Spring framework
- **Maven** - Multi-module project structure
- **Component-based architecture** - Modular design with focused responsibilities

### Core Components Structure

#### qy-core
The foundational module containing:
- Core utilities and lambda expressions
- Serialization utilities
- Global exception handling
- Must be built first before other modules

### Key Technologies
- **HuTool** - Chinese utility library for common operations
- **FastJSON2** - Alibaba's JSON processing
- **MyBatis-Plus** - Enhanced MyBatis with code generation
- **MapStruct** - Bean mapping framework
- **Jackson** - JSON serialization with JSR310 support
- **JWT** - Token-based authentication
- **Transmittable Thread Local** - Thread context propagation

### Development Patterns
- Component modules follow Spring Boot auto-configuration patterns
- Each component typically includes:
  - Main functionality in `src/main/java`
  - Test classes in `src/test/java`
  - Configuration files in `src/main/resources`