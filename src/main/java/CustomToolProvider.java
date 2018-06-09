import com.intellij.codeInspection.InspectionToolProvider;
import inspection.tool.GetterLocalInspectionTool;
import inspection.tool.Log4jLoggerInspectionTool;
import inspection.tool.NoArgsConstructorLocalInspectionTool;
import inspection.tool.SetterLocalInspectionTool;

/**
 * Created by 1 on 15.04.2018.
 */
public class CustomToolProvider implements InspectionToolProvider {
    private static final Class[] INSPECTIONS = {
            GetterLocalInspectionTool.class,
            NoArgsConstructorLocalInspectionTool.class,
            Log4jLoggerInspectionTool.class,
            SetterLocalInspectionTool.class
    };


    @Override
    public Class[] getInspectionClasses() {
        return INSPECTIONS;
    }
}
