package com.sky._sb0402;

import com.sky._sb0402.entity.MyData;
import com.sky._sb0402.repository.MyDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
class Sb0402ApplicationTests {

    @Autowired
    MyDataRepository myDataRepository;

    @Test
    void contextLoads() {
        IntStream.rangeClosed(1, 10).forEach(i->{
            MyData myData = MyData.builder()
                    .age(20+i)
                    .mail("kim"+i+"@korea.com")
                    .name("Kim"+i)
                    .memo("이것은 메모"+i)
                    .build();
            myDataRepository.save(myData);
        });
    }

    @Test
    void updateMyData() {
        MyData myData = MyData.builder()
                .id(1L)
                .age(31)
                .mail("kim31@korea.com")
                .name("Kim31")
                .memo("이것은 메모31")
                .build();
        myDataRepository.save(myData);
    }

    @Test
    void selectAllMyData() {
        List<MyData> list = myDataRepository.findAll();
        list.stream().forEach(m->{
            System.out.println(m);
        });
//        for (MyData myData : list) {
//            System.out.println(myData);
//        }
    }
    @Test
    void selectOne() {
        Optional<MyData> myData = myDataRepository.findById(100L);
        if (myData.isPresent()) {
            System.out.println(myData.get());
        } else {
            System.out.println("자료 없음");
        }
    }

    @Test
    void deleteOne() {
        myDataRepository.deleteById(1L);
    }

}
//
//contextLoads 메소드
//이 메소드는 IntStream을 사용하여 1부터 10까지의 숫자 범위로 반복하면서, 각 반복마다 새로운 MyData 객체를 생성하고 데이터베이스에 저장합니다. MyData 객체는 builder 패턴을 사용하여 생성되며, 각 객체는 고유한 age, mail, name, memo 값을 가집니다.
//
//        updateMyData 메소드
//이 메소드는 특정 ID(여기서는 1L)를 가진 MyData 객체를 새로운 값으로 업데이트하고 데이터베이스에 저장합니다. 이 경우, 새로운 age, mail, name, memo 값으로 객체를 업데이트합니다. JpaRepository의 save 메소드는 주어진 엔티티가 이미 존재한다면(기본 키가 데이터베이스에 이미 있으면) 업데이트를 수행합니다.
//
//selectAllMyData 메소드
//이 메소드는 데이터베이스에 저장된 모든 MyData 객체를 조회하여 출력합니다. findAll 메소드는 저장된 모든 엔티티의 리스트를 반환하며, 이 리스트를 스트림을 통해 각 객체를 출력합니다. 주석 처리된 부분은 같은 기능을 하는 for-each 루프를 보여줍니다.
//
//        selectOne 메소드
//이 메소드는 주어진 ID(100L)에 해당하는 MyData 객체를 데이터베이스에서 조회합니다. findById 메소드는 Optional<MyData> 객체를 반환하는데, 이는 조회된 데이터가 없을 경우를 처리하기 위한 것입니다. 데이터가 존재하면 출력하고, 그렇지 않으면 "자료 없음"을 출력합니다.
//
//deleteOne 메소드
//이 메소드는 주어진 ID(1L)를 가진 MyData 객체를 데이터베이스에서 삭제합니다. deleteById 메소드는 지정된 ID의 엔티티를 삭제합니다.
//
//이 코드는 Spring Data JPA를 사용하여 간단하고 선언적으로 데이터베이스 작업을 수행하는 방법을 보여줍니다. 데이터베이스 엔티티의 생명주기를 관리하는 기본적인 방법들을 포함하고 있으며, 실제 애플리케이션에서 유용하게 사용될 수 있습니다.