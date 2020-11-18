#### springboot介绍
		- 1.Springboot优点
			– 快速创建独立运行的Spring项目以及与主流框架集成
			– 使用嵌入式的Servlet容器，应用无需打成WAR包
			– starters自动依赖与版本控制
			– 大量的自动配置，简化开发，也可修改默认值
			– 无需配置XML，无代码生成，开箱即用
			– 准生产环境的运行时应用监控
			– 与云计算的天然集成
		- 2.Springboot简介
			- 简化Spring应用开发的一个框架；
			- 整个Spring技术栈的一个大整合；
			- J2EE开发的一站式解决方案；
		- 3.微服务
			- 微服务：架构风格
			- 一个应用应该是一组小型服务；可以通过http的方式进行互通
			- 单体应用：ALL IN ONE   开发测试简单、部署简单、水平扩展简单
			- 微服务：每一个功能元素最终都是一个可独立替换和独立升级的软件单元；
			- 详细参照微服务文档：ttps://martinfowler.com/articles/microservices.html#MicroservicesAndSoa
		- 4.准备
			– Spring框架的使用经验
			– 熟练使用Maven进行项目构建和依赖管理
			– 熟练使用Eclipse或者IDEA
			– jdk1.8
			– maven3.3以上
			– IntelliJ IDEA 2017
			- Spring Boot 1.5.9.RELEASE
		- 5.MAVEN设置；给maven 的settings.xml配置文件的profiles标签添加
			<profile>
			  <id>jdk-11</id>
			  <activation>
			    <activeByDefault>true</activeByDefault>
			    <jdk>11</jdk>
			  </activation>
			  <properties>
			    <maven.compiler.source>11</maven.compiler.source>
			    <maven.compiler.target>11</maven.compiler.target>
			    <maven.compiler.compilerVersion>11</maven.compiler.compilerVersion>
			  </properties>
			</profile>
		- 6.idea设置
			setting->Build,Execution,Deployment->Build Tools->Maven
			修改 Maven home direcory为自己的maven
			修改User setting file 和local repository
#### Spring Boot入门
		-1.Springboot helloworld：浏览器发送hello请求，服务器接受请求并处理，响应hello world字符串
			- 创建一个maven工程
			- 导入springboot相关依赖
			- 导入springboot依赖
				<parent>
			        <artifactId>spring-boot-starter-parent</artifactId>
			        <groupId>org.springframework.boot</groupId>
			        <version>2.3.5.RELEASE</version>
			    </parent>
			    <dependencies>
			        <dependency>
			            <groupId>org.springframework.boot</groupId>
			            <artifactId>spring-boot-starter-web</artifactId>
			            <version>2.3.5.RELEASE</version>
			        </dependency>
			    </dependencies>
			- 编写一个主程序；启动Springboot应用
				@SpringBootApplication
				/**
				 * @SpringBootApplication 来标注一个主程序类，说明是一个SpringBoot应用
				 */
				public class HelloWorldMainApplication {
				    public static void main(String[] args) {
				        //Spring应用启动起来
				        SpringApplication.run(HelloWorldMainApplication.class,args);
				        
				        
				    }
				}
			- 编写相关的Controller Service
				@Controller
				public class HelloController {

				    @ResponseBody
				    @RequestMapping("/hello")
				    public String hello(){
				        return "hello world";
				    }
				}
			- 运行主程序直接测试
			- 简化部署
				- 1 在pom导入插件
					<!--这个插件，可以将应用打包成一个可执行的jar包-->
				    <build>
				        <plugins>
				            <plugin>
				                <groupId>org.springframework.boot</groupId>
				                <artifactId>spring-boot-maven-plugin</artifactId>
				                <version>2.3.4.RELEASE</version>
				            </plugin>
				        </plugins>
				    </build>
				- 2 点击从右侧的maven->package打成jar包，直接使用java -jar 命令
			- Hello World探究
				- 1 pom文件
					* 1 父项目
						<parent>
					        <artifactId>spring-boot-starter-parent</artifactId>
					        <groupId>org.springframework.boot</groupId>
					        <version>2.3.5.RELEASE</version>
					     </parent>
					     他的父项目
					    <parent>
						    <groupId>org.springframework.boot</groupId>
						    <artifactId>spring-boot-dependencies</artifactId>
						    <version>2.3.5.RELEASE</version>
						</parent>
						他来真正管理Springboot应用里边的所以依赖
						Spring Boot的版本仲裁中心；以后我们导入依赖默认是不需要写版本；（没有在dependencies里面管理的依赖自然需要声明版本号）
					* 2 启动器
						<dependency>
				            <groupId>org.springframework.boot</groupId>
				            <artifactId>spring-boot-starter-web</artifactId>
				            <version>2.3.5.RELEASE</version>
				        </dependency>
				        spring-boot-starter-web：
				        	spring-boot-starter：Springboot的场景启动器；帮我们导入了web模块相关的依赖的组件；
				        SpringBoot将所有的功能场景都抽取出来，做成一个个的starters（启动器），只需要在项目里面引入这些starter相关场景的所有依赖都会导入进来。要用什么功能就导入什么场景的启动器
				- 2 主程序类
						@SpringBootApplication
						/**
						 * @SpringBootApplication 来标注一个主程序类，说明是一个SpringBoot应用
						 */
						public class HelloWorldMainApplication {
						    public static void main(String[] args) {
						        //Spring应用启动起来
						        SpringApplication.run(HelloWorldMainApplication.class,args);
						    }
						}
					@SpringBootApplication：Spring Boot应用标注在某个类上说明这个类是SpringBoot的主配置类，SpringBoot就应该运行这个类的main方法来启动SpringBoot应用；打开@SpringBootApplication：
						@Target({ElementType.TYPE})
						@Retention(RetentionPolicy.RUNTIME)
						@Documented
						@Inherited
						@SpringBootConfiguration
						@EnableAutoConfiguration
						@ComponentScan(
						    excludeFilters = {@Filter(
						    type = FilterType.CUSTOM,
						    classes = {TypeExcludeFilter.class}
						), @Filter(
						    type = FilterType.CUSTOM,
						    classes = {AutoConfigurationExcludeFilter.class}
						)}
						)
						public @interface SpringBootApplication {
					@SpringBootConfiguration：SpringBoot的配置类；
						标注在某个类上，表示这是一个Spring Boot的配置类；
							配置类=配置文件；配置类也是容器中的一个组件；@Component
					@EnableAutoConfiguration：开启自动配置功能；
						以前我们需要配置的东西，Spring Boot帮我们自动配置；@EnableAutoConfiguration告诉SpringBoot开启自动配置功能；这样自动配置才能生效；
					点进入@EnableAutoConfiguration：
					@AutoConfigurationPackage
					@Import({AutoConfigurationImportSelector.class})
					public @interface EnableAutoConfiguration {
						@AutoConfigurationPackage：自动配置包
						点进@AutoConfigurationPackage:
						@Import({Registrar.class}):给容器导入组件；导入的组件由Registrar.class
							将主配置类（@SpringBootApplication）的所在包及下面所有子包里面的所有组件扫描到Spring容器；


​						
​							







  