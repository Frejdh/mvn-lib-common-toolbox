Changelog
-

# 1.3.0
* Updated dependencies and parent.
* Added GitHub workflows for CI/CD.
* Renamed classes
  * `ThrowableUtils` to `Conditionals`.
  * `NullSafe` to `Operators`.
  * `Condition` to `AbstractCondition`.
  * `EqualsCondition` to `EqualsValueCondition`.
* Renamed method `NullSafe.safe()` to `Operators.safeCall()`.
* Moved method `replaceIllegalFilenameCharacters()` from `CommonUtils` to `OperatingSystemUtils`.
* Extended `Conditionals` class to support custom predicates, and to check for blank values.
* More unit tests.
* Now uses external [Frejdh/releases](https://github.com/Frejdh/releases) repository for 'Frejdh' dependencies.

# 1.2.0
* Fixed `firstNonNull` method in `NullSafe`. Previously a null value could still be returned in some scenarios.
* Optimized performance of `ThrowableUtils` (and `NullSafe`) classes. Removed unnecessary method invocations.
* Added `equalsToAny` method in `ThrowableUtils` class.
* Added some more unit tests for `NullSafe`

# 1.1.0
* Dependency versions now defined in parent instead for better sync between projects.
* Parameterized some tests.

# 1.0.8
* Added `firstNonNull` methods in `NullSafe` helper class for nested elvis-like cases.

# 1.0.7
* Added support for JDK versions above JDK 8 (tested with JDK 17).

# 1.0.6
* Upgraded parent and dependency versions.
  * Also removed spring-boot as a dependency.
* Added an additional `NullSafe` method.

# 1.0.5
* Added class `ThrowingRunnable`.

# 1.0.4
* Added class `NullSafe`
* Moved `ThrowableUtils.safeNullPointerOperation(operation)` method to `NullSafe.safe(operation)`
* Added `NullSafe.elvis(operation, defaultValue)` method.

# 1.0.3
* Added `ThrowableUtils` class
* Added `SneakyThrow` method
* Added more reflection methods

# 1.0.2
* Stacktrace method now accepts `Throwable` objects instead of just `Exception`.

# 1.0.1
* Added more methods to `OperatingSystemUtils` and `CommonUtils`
* Added `FileUtils`
* Improved `SerializeUtils` methods. More overloading methods.
* Renamed `NothingAvailableException` to `NothingFoundException`
* Get stacktrace method in `ReflectionUtils`. Small improvements.

# 1.0.0
First version
