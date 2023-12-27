# Spring Boot Elasticsearch 8.18

## Elasticsearch Repository

- Spring Data JPA repository 처럼 간단한 쿼리를 사용할 수 있다.
  - [ElasticsearchRepository](https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/repositories/elasticsearch-repositories.html)

## Elasticsearch Operations

- 좀 더 복잡한 쿼리는 [Criteria Query](https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/template.html#elasticsearch.operations.criteriaquery)를 사용할 수 있다.
- Criteria Query로도 표현하기 힘든 복잡한 쿼리는 [Native Query](https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/template.html#elasticsearch.operations.nativequery)를 사용할 수 있다.

## Reference Documentation

- Breaking Changes 꼼꼼히 읽기
  - [Release Notes](https://github.com/spring-projects/spring-data-elasticsearch/releases)
  - [Upgrading from 4.4.x to 5.0.x](https://docs.spring.io/spring-data/elasticsearch/docs/5.1.6/reference/html/#elasticsearch-migration-guide-4.4-5.0)
  - [Upgrading from 5.0.x to 5.1.x](https://docs.spring.io/spring-data/elasticsearch/docs/5.1.6/reference/html/#elasticsearch-migration-guide-5.0-5.1)
  - [Upgrading from 5.1.x to 5.2.x](https://docs.spring.io/spring-data/elasticsearch/reference/migration-guides/migration-guide-5.1-5.2.html)
