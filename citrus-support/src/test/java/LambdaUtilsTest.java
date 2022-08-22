import com.github.yiuman.citrus.support.crud.query.Query;
import com.github.yiuman.citrus.support.utils.LambdaUtils;
import org.junit.jupiter.api.Test;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;

/**
 * @author yiuman
 * @date 2021/8/20
 */
public class LambdaUtilsTest {

    public LambdaUtilsTest() {
    }

    @Test
    public void serialized() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        SerializedLambda serialized = LambdaUtils.serialized(Query::getConditions);
        System.out.println(serialized);
    }

}
