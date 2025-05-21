# Forkast - Smart Recipe Recommendation App

Forkast is a modern recipe recommendation application that helps users discover and manage recipes based on their available ingredients and preferences.

## Features

- User authentication and profile management
- Smart recipe recommendations based on available ingredients
- Recipe bookmarking and management
- Step-by-step cooking instructions with timers
- Ingredient tracking and usage history
- Personalized recipe suggestions

## Tech Stack

- **Backend**: Java Spring Boot
- **Database**: MySQL
- **Security**: Spring Security
- **Frontend**: (Coming soon) React.js
- **Design**: Figma

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Gradle 7.0 or higher

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/forkast.git
   cd forkast
   ```

2. Set up the database:
   ```sql
   CREATE DATABASE forkast;
   CREATE DATABASE forkast_test;
   ```

3. Configure the application:
   - Copy `backend/src/main/resources/application.yml.example` to `application.yml`
   - Update the database credentials in `application.yml`

4. Build and run the application:
   ```bash
   cd backend
   ./gradlew build
   ./gradlew bootRun
   ```

## Development

### Running Tests

```bash
./gradlew test
```

### Code Style

The project follows Google Java Style Guide. To format your code:

```bash
./gradlew spotlessApply
```

## API Documentation

API documentation is available at `/swagger-ui.html` when running the application.

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Spring Boot team for the amazing framework
- All contributors who have helped shape this project
