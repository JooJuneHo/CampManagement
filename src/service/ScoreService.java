package service;

import domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScoreService {


    private Scanner sc = new Scanner(System.in);
    private KeepGrade keepGrade = new KeepGrade();



    // 메서드 이름 변경
    public void registerScore(StudentList studentList, SubjectList subjectList, ScoreList scoreList) {



        System.out.println("성적을 입력할 학생을 고르세요.");

        for (Student student : studentList.getStudentList()) {
            System.out.println(student.getStudentId() + ". " + student.getName());
        }

        // 성적 입력할 학생 입력
        Integer studentSelector = sc.nextInt();


        // 등록된 학생이 아닐 경우를 처리
        checkStudent(studentList,subjectList,studentSelector-1, scoreList);


        System.out.println("성적을 입력할 과목을 고르세요.");


        // #### 현재 과목전체를 보여줌 => 수강하고있는 과목만 출력하도록. ####
        // 위에서 입력한 학생의 Student 객체
        Student studentSelect = studentList.findStudentById(studentSelector-1);


        for(int i=0; i<studentSelect.getSubjectList().getSubjectList().size(); i++){
            System.out.println(studentSelect.getSubjectList().findSubjectById(i).getSubjectId() + ". " + studentSelect.getSubjectList().findSubjectById(i).getName());
        }

        // 성적 입력할 과목 입력
        int subjectSelector = sc.nextInt();

        // 수강중인 과목이 아닐 경우를 처리
        checkSubject(studentList, subjectList, subjectSelector, studentSelect, scoreList);

        // 회차 입력 x => 자동으로 회차 출력, 점수 등록
        List<Integer> score = new ArrayList<>();

        for(int round=0; round<3; round++){
            System.out.print(round+1 + "회차 점수 입력 : ");
            score.add(sc.nextInt());

            // Score(subjectId, studentId, testRound, score) 객체를 생성해서 scoreList에 담기
            Score addedScore = new Score(subjectSelector - 1, studentSelector - 1, round+1, score.get(round));
            addedScore.addGrade(keepGrade.keepScore(addedScore, subjectList.findSubjectById(subjectSelector - 1)));
            scoreList.addScore(addedScore);
        }

        // scoreList에 들어있는 값 출력
        print(scoreList.getScoreList(),studentList,subjectList,studentSelector-1,subjectSelector-1);

    }

    // 등록된 학생이 아닐 경우를 처리
    private void checkStudent(StudentList studentList, SubjectList subjectList,Integer studentSelector, ScoreList scoreList){
        boolean studentCheck = true;
        if(studentSelector >= studentList.getStudentList().size()){
            studentCheck = false;
        }
        if(!studentCheck){
            System.out.println("등록된 학생이 아닙니다.");
            registerScore(studentList,subjectList, scoreList);
        }
    }

    // 수강중인 과목이 아닐 경우를 처리
    private void checkSubject(StudentList studentList, SubjectList subjectList, Integer subjectSelector, Student studentSelect, ScoreList scoreList){
        boolean subjectCheck = false;
        for (int i=0; i<studentSelect.getSubjectList().getSubjectList().size(); i++){
            if(subjectSelector == studentSelect.getSubjectList().findSubjectById(i).getSubjectId()){
                subjectCheck = true;
            }
        }
        if(!subjectCheck){
            System.out.println("수강하고 있는 과목이 아닙니다.");
            registerScore(studentList,subjectList,scoreList);
        }
    }


    // 입력한 성적 출력
    private void print(List<Score> scoreList, StudentList studentList, SubjectList subjectList , int studentId, int subjectId){
        System.out.println("이름 : " + studentList.findStudentNameById(studentId)
                + " , 과목 : " + subjectList.findSubjectNameById(subjectId));
        for (Score score : scoreList) {
            System.out.println("회차 : " + score.getTestRound()
                    + " , 점수 : " + score.getScore());
        }
        System.out.println("성적이 등록 되었습니다.");
    }

    public void examineStudentGradeBySpecificRound(StudentList studentRepository,ScoreList scoreRepository){
        studentRepository.showStudentList();
        System.out.print("조회할 수강생의 Id값을 고르시오 : ");
        int selectedStudentId = sc.nextInt() - 1;
        Student findStudent = studentRepository.findStudentById(selectedStudentId);
        findStudent.showSubjectList();
        System.out.print("조회할 과목의 Id 값을 고르시오 : ");
        int selectedSubjectId = sc.nextInt() - 1;
        System.out.print("조회할 회차를 입력하세요 : ");
        int selectedTestNumber = sc.nextInt();
        for (Score score : scoreRepository.getScoreList()) {
            if(score.getStudentId() == selectedStudentId && score.getSubjectId() == selectedSubjectId && score.getTestRound() == selectedTestNumber){
                System.out.println(score.getGrade());
            }
        }
    }
}
