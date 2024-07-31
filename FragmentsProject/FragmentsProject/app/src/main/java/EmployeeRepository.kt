import com.example.fragmentsproject.dao.EmployeeDataDao
import com.example.fragmentsproject.data.EmployeeData

class EmployeeRepository(private val dao: EmployeeDataDao) {

    suspend fun addEmp(employeeData: EmployeeData)
    {
        dao.addEmployeeDetail(employeeData)
    }

}