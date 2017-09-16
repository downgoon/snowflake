# Snowflake

java edition of [Twitter Snowflake](https://github.com/twitter/snowflake), a network service for generating unique ID numbers at high scale with some simple guarantees.

## QuickStart

- import maven dependency

```
<dependency>
  <groupId>xyz.downgoon</groupId>
  <artifactId>snowflake</artifactId>
  <version>1.0.0</version>
</dependency>
```

- sample code

``` java
// datacenter: 2; workerId: 5
Snowflake snowflake = new Snowflake(2, 5);
long id1 = snowflake.nextId();
long id2 = snowflake.nextId();
```

## snowflake tutorial

- [snowflake tutorial](docs/SnowflakeTutorial_zh_CN.md)：中文教程
