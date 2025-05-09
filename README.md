# HQL-Fabric
> ✨ A flexible and extensible query builder + executor for Hibernate(JPA), designed to simplify HQL construction and improve data access control.

**HQL-Fabric** 
- Offers a reusable and decoupled approach to dynamically building and executing HQL queries, built on top of Hibernate and JPA. It aims to solve common pain points in enterprise development involving custom queries and dynamic conditions.
- It also **abstract away the differences in query syntax and capability across various database vendors**, providing a unified, vendor-agnostic data access layer. This eliminates the need to introduce multiple capability solutions just to support different underlying databases.

--- 

## 💡 Project Purpose 
When using Spring Data JPA + Hibernate in typical projects, developers often encounter the following issues: 
- Each entity requires its own Repository interface, increasing code and maintenance overhead;
- Custom business queries rely heavily on `@Query`, `@Modifying`, `@Transactional`, which can be verbose and limiting;
- Dynamic queries using `JpaSpecificationExecutor` involve callback lambdas like `cb.equal`, `cb.or`, which are hard to read and debug;
- Query logic and parameter binding are scattered, reducing traceability and reusability;
- Data layer logging and transaction handling are fragmented and hard to centralize;
- Difficult to extend to features like database sharding, routing, or custom session handling.

---

## ✅ Problems Solved by hql-fabric

| Problem | hql-fabric Solution |
|---------|---------------------|
| Hard to write dynamic queries | DSL-style builder for chaining and composing HQL statements (similar to MyBatis) |
| Low reusability of HQL | Decouples query construction and execution via `QueryRequest` |
| Unclear parameter binding | Explicit exposure of `Map<String, Object>` parameters for debugging |
| Repository bloat | Avoids the need for entity-specific Repositories by using a generic `PersistenceService` |
| Scattered query logging | Centralized execution layer for consistent logging and monitoring |
| Difficult to prepare for sharding | Extensible architecture that supports dynamic routing and multi-database patterns |

---

## 🧱 Design Philosophy

- **Builder pattern** to encapsulate HQL construction;
- **QueryRequest model** to carry the built HQL and parameters;
- **PersistenceService** as a unified query executor;
- Ready for extensions like pagination, sorting, DTO projections, sharding, read/write separation;
- Preserves fine-grained Hibernate session control (flush, clear, rollback, etc.);
- Supports plugin points for data-layer logging, query mocking, slow-query detection, etc.


### UML Diagrams (to be refine via omniGraffle) 
```
  +-------------------------+
  |     HqlQueryBuilder     |  <== Responsible for building HQL query strings
  +-------------------------+
  | - hqlParts: List<String>|
  | - params: Map<String, Object>|
  +-------------------------+
  | + from(Class<?> clazz)  |
  | + eq(String, Object)    |
  | + and()                 |
  | + build(): String       |
  | + getInjectionParameters(): Map|
  | + clear()               |
  +-------------------------+

            |
            | build() + getInjectionParameters()
            v

  +-------------------------+
  |      QueryRequest       |  <== Query bridge object
  +-------------------------+
  | - hql: String           |
  | - parameters: Map       |
  +-------------------------+
  | + getHql()              |
  | + getParameters()       |
  | + static from(builder)  |
  +-------------------------+

            |
            | query(hql, parameters)
            v

  +-------------------------+       +-------------------------+
  |  IHqlQueryService       |<------+   HqlQueryService    |
  +-------------------------+       +-------------------------+
  | + query(String, Map)    |       | Implements: query()     |
  | + query(QueryRequest)   |       | Uses Hibernate to execute HQL |
  +-------------------------+       +-------------------------+

            ^
            |
  +-------------------------+
  |     Caller Service       |
  +-------------------------+
  | Uses builder to create query |
  | Constructs QueryRequest      |
  | Invokes queryService.query() |
  +-------------------------+
```


---

## 📦 Planned Module Structure

- `hql-fabric-core`: Query builder and execution core;
- `hql-fabric-sharding`: Dynamic DataSource routing and sharding support;
- `hql-fabric-monitor`: Optional observability/logging/metrics module;
- `hql-fabric-example`: Show how to use `HqlQueryBuilder` + `IHqlQuerySerivce` to implement complex SQL query languages, and also show how to enable table sharding query in multiple datasources.  
---

## 🔧 Use Cases

- Replacing boilerplate JPA repository interfaces;
- Writing flexible cross-entity dynamic queries;
- Providing reusable data-layer APIs for reporting or exporting;
- Controlling HQL and session behavior at a fine-grained level;
- Transitioning to database sharding or scaling SQL control in large systems;

---
## 🧩 Example Usage (coming soon)

To be added:

- Basic builder usage
- Composable HQL conditions
- How to execute queries via `PersistenceService`
- Spring Boot integration guide
- Sharding/routing extension usage (Stage 2)

### Integration Guide: Spring BOot + JPA Projects
To use **HQL-Fabric** in your JPA-based Spring Framework projects:
- Add HQL-Fabric as a dependency (once available in Maven Central or via local build):

```xml
<!-- Coming soon -->
<dependency>
    <groupId>com.hql.fabric</groupId>
    <artifactId>hql-fabric</artifactId>
    <version>1.0.0</version>
</dependency>
```

- Inject and user PersistenceService to execute your HQL dynamically:
```java
@Autowired
private PersistenceService persistenceService; 
```

- Build dynamic queries using HqlQueryBuilder:
```java
HqlQueryBuilder builder = new HqlQueryBuilder();
String hql = builder
  .from(User.class, "user")
  .eq("user.status", "ACTIVE")
  .like("user.username", "%john%")
  .orderByAsc("user.createdAt")
  .build();

// hand over your combined hql to PersistenceService to execute via Hibernate session & transaction
Map<String, Object> params = buider.getInjectionParameters();

List<User> users = persistenceService.query(hql, params); 
```

### Migration Tips: Moving from Traditional Repository to HQL-Fabric
If you're migrating from a traditional JPA Repository setup (e.g., using `JpaRepository`, `@Query`, `JpaSpecificationExecutor`), consider the following: 

#### Problem: Repository Bloat 
- **Before**: Every entity requires its own interface.
- **Now**: One unified dynamic query engine using `HqlQueryBuilder`. 

#### Problem: Complex Dynamic Queries 
- **Before**: Hard to compose nested or optional filters with `Specification<T>` or criteria builders. 
- **Now**: Fluent API makes optional conditions, joins, and parameter binding effortless. 

#### Problem: Vendor-specific workarounds
- **Before**: Needed to write custom SQL/HQL for Oracle, MySQL, Postgres, etc. 
- **Now**: Build vendor-agnostic HQL using a unified DSL.

### Example 1: Basic Builder Usage
```java
HqlQueryBuilder builder = new HqlQueryBuilder()
     .from(Product.class, "p")
     .eq("p.available", true)
     // greater than 
     .gt("p.price", 100);

String hql = builder.build();
Map<String, Object> params = builder.getInjectionParameters();
// hand over hql & parameters -> PersistenceService -> Hibernate#Session
List<Product> results = persistenceService.query(hql, params); 
```

### Example 2: Composable Conditions 
```java
builder
    .open() 
       .like("u.name", "%john%")
       .or()
       .like("u.displayName", "%john%")
   .close()
   .eq("u.status", "ACTIVE"); 
```

### Example 3: Executing via PersistenceService 
```java
List<User> users = persistenceService.query(
     builder.build(),
     builder.getInjectionParameters()
); 
```


### Example 4: Spring Boot Configuration Tips
- Define `PersistenceService` as a Spring Bean in correspoinding config file. 
- Register entity manager or inject `SessionFactory` to `PersistenceService`.

// todo: add this coming soon

- Customize global logging/metrics via Spring AOP if needed.

---
## 🌐 Native SQL Extension Interface Support (v1.1)
### Background 

### UML Design (with Hibernate SessionFactory Injection)
This design introduces a dedicated service interface `NativeQueryService`, which provides methods to execute native SQL queries. THe concrete implementation `MySQLNativeQueryService` inherits from the base `HqlQueryService` to reuse it's inner `SessionFactory` instance, ensuring consistent and testable query execution. 

### Key Interface Definitions 
- `NativeQueryService`: Defines generic methods for native SQL execution with or without a request object. 
- `NativeQueryRequest`: Encapsulate raw SQL string and parameters maps for query execution. 
- `HqlQueryService`: Base class containing reusable `SessionFactory` logic and session retrieval methods. 
- `MySQLNativeQueryService`: A concrete implementation for MySQL that extends `HqlQueryService` and implements `NativeQueryService`. 

### Usage & Injection 
This structure allows the `SessionFactory` to be injected via constructor, making it compatible with dependency injection frameworks such as Spring. It encourages a clean service layering, supports testing, and can be extended easily for other RDBMS backends (e.g., PostgreSQL). 

- Example (Spring-style bean configuration):
```java
@Bean("mysqlNativeQueryService")
public NativeQueryService nativeQueryService(SessionFactory sessionFactory) {
  return new MySQLNativeQueryService(sessionFactory; 
}
```
--- 

## 🧱 Tech Stack

- Java 8+
- Spring Boot 2.x / 3.x
- Hibernate ORM
- JPA
- Lombok (optional)
- Spring AOP (for routing and logging)

---

## 📍 About the Name

> “Fabric” is inspired by woven fabric systems—symbolizing how this project can **weave together, control, and route HQL queries in a flexible and unified way.**

---

If you're interested in this project, feel free to open issues, send pull requests, or ⭐️ Star it!



