package tn.esprit.devops_project.services.ServiceTest;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.github.springtestdbunit.annotation.DatabaseSetup;
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
import tn.esprit.devops_project.controllers.OperatorController;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.services.OperatorServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;








@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@DatabaseSetup("/data-set/operator-data.xml")
class OperatorServiceImplTest {

    @Autowired
    private OperatorServiceImpl operatorService;

    @Test
    void retrieveAllOperators() {
        final List<Operator> allOperators = this.operatorService.retrieveAllOperators();
        assertEquals(allOperators.size(), 2);
    }

    @Test
    void addOperator() {
        final Operator operator = new Operator(3L,"aa","bb","azerty",null);

        Operator savedOperator = this.operatorService.addOperator(operator);

        assertEquals(this.operatorService.retrieveAllOperators().size(), 3); // Adjust the expected size as needed.
        assertEquals(savedOperator.getFname(), "aa");
    }

    @DatabaseSetup("/data-set/operator-data.xml")
    @Test
    void deleteOperator() {
        Operator operator = this.operatorService.retrieveOperator(1L);
        this.operatorService.deleteOperator(operator.getIdOperateur());
    }

    @Test
    void updateOperator() {
        // Replace 1L with the ID of an existing operator to update.
        Operator existingOperator = this.operatorService.retrieveOperator(1L);
        existingOperator.setFname("UpdatedFirstName");
        Operator updatedOperator = this.operatorService.updateOperator(existingOperator);

        assertEquals(updatedOperator.getFname(), "UpdatedFirstName");
    }

    @Test
    void retrieveOperator() {
        final Operator operator = this.operatorService.retrieveOperator(1L);
        assertEquals("John", operator.getFname()); // Adjust the expected name as needed.
    }

    @Test
    void retrieveOperator_NullId() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            final Operator operator = this.operatorService.retrieveOperator(100L);
        });
    }
}
