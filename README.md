# JkuMensaParser
This is a parser for the diverse mensas present around [JKU] (http://www.jku.at/).
Currently [Classic, Choice](http://menu.mensen.at/index/index/locid/1), [KHG](http://www.khg-linz.at/?page_id=379) and [Raab](http://www.sommerhaus-hotel.at/de/restaurant_plan.php) are supported.

## Usage
You can either use the MainFactory as a shortcut
```java
List<Menu> menus = MainFactory.newMain(Type.CLASSIC);
```
or build everything yourself
```java
List<Menu> menus = JkuClassicFactory(new JkuClassicSourceFactory()).newMensa();
```

The [JkuMensaApi](https://github.com/Nithanim/JkuMensaApi) is required to function.

## License
The JkuMensaParser is licensed under the LGPL. See LICENSE.txt for more information.
