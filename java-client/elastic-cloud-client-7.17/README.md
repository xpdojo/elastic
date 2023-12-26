# Demo project for Elastic Cloud Java Client

## package structure

```shell
.
├── app-demo
└── validation
└── vehicle
    ├── api-search
    └── infra-elasticsearch
```

## jar 빌드 후 실행

```shell
./gradlew clean :demo:build
java -jar demo/build/libs/demo-0.0.1-SNAPSHOT.jar
```

## Local 환경에서 실행

```shell
./gradlew clean :demo:bootRun -P profile=dev
```

## Checkstyle

- [The Checkstyle Plugin](https://docs.gradle.org/7.5.1/userguide/checkstyle_plugin.html) - Gradle 7.5.1

```shell
./gradlew check
```

## Test

```shell
./gradlew clean test
```

## 참조

- [실전! 멀티 모듈 프로젝트 구조와 설계](https://www.inflearn.com/course/infcon2022/unit/126503) - 김대성, 네이버 뮤직 플랫폼
- [우아한 멀티모듈](https://youtu.be/nH382BcycHc) - 권용근, 우아한형제들
  - [멀티모듈 설계 이야기 with Spring, Gradle](https://techblog.woowahan.com/2637/) - 권용근, 우아한형제들
