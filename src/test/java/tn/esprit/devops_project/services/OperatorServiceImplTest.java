package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.repositories.OperatorRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class OperatorServiceImplTest {

    @Autowired
    private OperatorServiceImpl operatorService;

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void addOperator() {
        final Operator operator = new Operator();
        operator.setFname("John");
        operator.setLname("Doe");
        operator.setPassword("password");

        Operator savedOperator = this.operatorService.addOperator(operator);

        assertNotNull(savedOperator.getIdOperateur());
        assertEquals(savedOperator.getFname(), "John");
        assertEquals(savedOperator.getLname(), "Doe");
        assertEquals(savedOperator.getPassword(), "password");
    }


    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveOperator() {
        final Operator operator = this.operatorService.retrieveOperator(1L);
        assertEquals("John", operator.getFname());
        assertEquals("Doe", operator.getLname());
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveAllOperators() {
        final List<Operator> allOperators = this.operatorService.retrieveAllOperators();
        assertEquals(allOperators.size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void updateOperator() {
        final Operator operator = this.operatorService.retrieveOperator(1L);
        operator.setFname("Jane");
        operator.setLname("Doe");

        Operator updatedOperator = this.operatorService.updateOperator(operator);

        assertEquals(updatedOperator.getFname(), "Jane");
        assertEquals(updatedOperator.getLname(), "Doe");
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void deleteOperator() {
        this.operatorService.deleteOperator(1L);
        assertThrows(NullPointerException.class, () -> this.operatorService.retrieveOperator(1L));
    }
}
