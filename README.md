# Create and Configure Pipeline job in Jenkins

This project now includes a Jenkins pipeline to automate the CI/CD process using **Jenkins**. The pipeline defines stages to clean, test, and build the project using **Maven** and **OpenJDK 11**.

---

## Pipeline Stages
**1. clean:**  
* Cleans the Maven project using the command: `mvn clean`.

**2. test:**  
* Runs the unit tests using: `mvn test`.

**3. build:**  
* Builds the project while skipping tests with: `mvn install -DskipTests`.

**4. Scan**
* Performs static code analysis with SonarQube using `mvn sonar:sonar`.

---

## Requirements
To run this pipeline successfully, ensure that the following tools are installed and configured on your Jenkins server:  
* **Maven 3.9.9**
* **OpenJDK 11**

---

## Student

- **Name:** _Lim Xing Yi_
- **Date Completed:** _31 July 2025_
- **Course/Lab:** _SD in DevOps - Lesson 15: e-Learning_
