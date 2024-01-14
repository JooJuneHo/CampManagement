package service;

import domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScoreService {

    private ScoreList scoreList = new ScoreList();

    private Scanner sc = new Scanner(System.in);

    // 메서드 이름 변경
    public void registerScore(StudentList studentList, SubjectList subjectList) {


        CreateSubjectsThatStudentListening add = new CreateSubjectsThatStudentListening();
        add.createSubjectsThatStudentListening(studentList,subjectList);


        System.out.println("성적을 입력할 학생을 고르세요.");

        for (Student student : studentList.getStudentList()) {
            System.out.println(student.getStudentId() + ". " + student.getName());
        }

        // 성적 입력할 학생 입력
        Integer studentSelector = sc.nextInt();


        // 등록된 학생이 아닐 경우를 처리
        checkStudent(studentList,subjectList,studentSelector);


        System.out.println("성적을 입력할 과목을 고르세요.");


        // #### 현재 과목전체를 보여줌 => 수강하고있는 과목만 출력하도록. ####
        // 위에서 입력한 학생의 Student 객체
        Student studentSelect = studentList.findStudentById(studentSelector);
        // 질문 : findStudentById() 에서 studentList.get(index) ==> index값을 입력받는거 아닌가요??? TODO
        // studentSelector-1 이 들어가야할 것 같은데 studentSelector를 넣으니 정상적으로 작동하네요ㅠㅠ

        for(int i=0; i<studentSelect.getSubjectList().getSubjectList().size(); i++){
            System.out.println(studentSelect.getSubjectList().findSubjectById(i).getSubjectId() + ". " + studentSelect.getSubjectList().findSubjectById(i).getName());
        }

        // 성적 입력할 과목 입력
        int subjectSelector = sc.nextInt();

        // 수강중인 과목이 아닐 경우를 처리
        checkSubject(studentList, subjectList, subjectSelector, studentSelect);

        // 회차 입력 x => 자동으로 회차 출력, 점수 등록
        List<Integer> score = new ArrayList<>();
        int round = 1;
        for(int i=0; i<10; i++){
            System.out.print(round + "회차 점수 입력 : ");
            score.add(sc.nextInt());

            // Score(subjectId, studentId, testRound, score) 객체를 생성해서 scoreList에 담기
            scoreList.setScoreList(new Score(subjectSelector-1,studentSelector-1,round,score.get(i)));
            round++;
        }


        // scoreList에 들어있는 값 출력
        print(scoreList.getScoreList(),studentList,subjectList,studentSelector-1,subjectSelector-1);

    }

    // 등록된 학생이 아닐 경우를 처리
    private void checkStudent(StudentList studentList, SubjectList subjectList,Integer studentSelector){
        boolean studentCheck = true;
        for(int i=0; i<studentList.getStudentList().size(); i++){
            if(studentSelector > studentList.getStudentList().size()){
                studentCheck = false;
            }
        }
        if(!studentCheck){
            System.out.println("등록된 학생이 아닙니다.");
            registerScore(studentList,subjectList);
        }
    }

    // 수강중인 과목이 아닐 경우를 처리
    private void checkSubject(StudentList studentList, SubjectList subjectList, Integer subjectSelector, Student studentSelect){
        boolean subjectCheck = false;
        for (int i=0; i<studentSelect.getSubjectList().getSubjectList().size(); i++){
            if(subjectSelector == studentSelect.getSubjectList().findSubjectById(i).getSubjectId()){
                subjectCheck = true;
            }
        }
        if(!subjectCheck){
            System.out.println("수강하고 있는 과목이 아닙니다.");
            registerScore(studentList,subjectList);
        }
    }

    // id로 student 이름 조회
    private String findStudentNameById(StudentList studentList, int studentId){
        return studentList.getStudentList().get(studentId).getName();
    }

    // id로 subject 이름 조회
    private String findSubjectNameById(SubjectList subjectList, int subjectId){
        return subjectList.getSubjectList().get(subjectId).getName();
    }

    // 입력한 성적 출력
    private void print(List<Score> scoreList, StudentList studentList, SubjectList subjectList , int studentId, int subjectId){
        System.out.println("이름 : " + findStudentNameById(studentList,studentId)
                + " , 과목 : " + findSubjectNameById(subjectList,subjectId));
        for (Score score : scoreList) {
            System.out.println("회차 : " + score.getTestRound()
                    + " , 점수 : " + score.getScore());
        }
        System.out.println("성적이 등록 되었습니다.");
    }

}
