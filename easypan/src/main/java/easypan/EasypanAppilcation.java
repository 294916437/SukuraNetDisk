package easypan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableAsync
@SpringBootApplication(scanBasePackages = {"easypan"})
@MapperScan(basePackages= {"easypan.mappers"})
@EnableTransactionManagement
@EnableScheduling
public class EasypanAppilcation {
	public static void main(String[] args) {
	SpringApplication.run(EasypanAppilcation.class, args);
	}
}
