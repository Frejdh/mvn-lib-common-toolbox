Toolbox for mixed purposes
-
This module is meant as a utility for general coding, OS compatibility and serializing with some helpful
methods and exceptions.
All available classes can be found [here](https://github.com/Frejdh/mvn-lib-common-toolbox/tree/master/src/main/java/com/frejdh/util/common).

## Program arguments
To print out request/parsing times in the console, use: `--captureTimes` <br>

## Adding the dependency
```
<dependencies>
    <dependency>
        <groupId>com.frejdh.util.common</groupId>
        <artifactId>common-toolbox</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>

<repositories> <!-- Required in order to resolve this package -->
    <repository>
        <id>mvn-lib-common-toolbox</id>
        <url>https://raw.github.com/Frejdh/mvn-lib-common-toolbox/mvn-repo/</url>
    </repository>
</repositories>
```