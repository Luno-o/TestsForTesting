package restservicestests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import restservicestests.pojo.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetReqStoreApiTest {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String BASE_PET = "/pet";
    private static final String BASE_STORE = "/store";
    private static final String BASE_USER = "/user";
    private static final ArrayList<String> photoUrls= new ArrayList<>(1);
    private static final ArrayList<Tag> tags= new ArrayList<>(1);
    private static final ArrayList<String> statusList = new ArrayList<>(1);
    @BeforeEach
    public void setUp(){

    }
    @DisplayName("Проверка получения питомца")
    @Test
    public void getPet(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_PET),Specs.responseSpecification());
        given()
                .when().get("/2").then()
                .statusCode(200).extract().response().as(PetReq.class);
    }

    @Test
    @DisplayName("Проверка вставки питомца")
    public void postPet(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_PET),
                Specs.responseSpecification());
        photoUrls.clear();
        photoUrls.add("string");
        tags.clear();
        tags.add(new Tag(200,"name"));
        statusList.clear();
        PetReq petReq = new PetReq(200, new Category(),
                "Melisa", photoUrls,tags,"available");
        given()
                .when()
                .header("api_key","special-key")
                .body(petReq).post()
                .then().statusCode(200)
        ;
        given()
                .when().get("/"+petReq.getId()).then()
                .statusCode(200).body("name",equalTo(petReq.getName()));
    }
    @Test
    @DisplayName("Проверка обновления питомца")
    public void putPet(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_PET),
                Specs.responseSpecification());
        PetReq petReq = new PetReq(200, new Category(),
                "Melisa", photoUrls,tags,"available");
        PetReq petReq2 = new PetReq(200, new Category(),
                "Melisas", photoUrls,tags,"available");
        given()
                .when()
                .header("api_key","special-key")
                .body(petReq).post()
                .then().statusCode(200)
        ;
        given()
                .when()
                .header("api_key","special-key")
                .body(petReq2).put()
                .then().statusCode(200).body("name",equalTo(petReq2.getName()))
        ;
    }
    @Test
    @DisplayName("Проверка удаления питомца")
    public void deletePet(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_PET),
                Specs.responseSpecification());
        PetReq petReq = new PetReq(200, new Category(),
                "Melisa", photoUrls,tags,"available");

        given()
                .when()
                .header("api_key","special-key")
                .body(petReq).post()
                .then().statusCode(200)
        ;
        given()
                .when()
                .header("api_key","special-key")
                .get("/"+petReq.getId())
                .then().statusCode(200);
        given()
                .when()
                .header("api_key","special-key")
                .delete("/"+petReq.getId())
                .then().statusCode(200);
        given()
                .when()
                .header("api_key","special-key")
                .get("/"+petReq.getId())
                .then().statusCode(404);

    }
    @DisplayName("Проверка поиска по статусам")
    @ParameterizedTest
    @ValueSource(strings ={"available","pending","sold"})
    public void getPetsByStatus(String status){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_PET),
                Specs.responseSpecification());

        statusList.add(status);
        given()
                .when()
                .header("api_key","special-key")
                .queryParam("status", statusList)
                .get("/findByStatus")
                .then().statusCode(200).body("status[0]",equalTo(status));
    }
    @DisplayName("Проверка получения инвентаря")
    @Test
    public void getInventory(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_STORE),Specs.responseSpecification());
        given()
                .when().get("/inventory").then()
                .statusCode(200);
    }
    @DisplayName("Проверка размещения заказа")
    @Test
    public void postOrder(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_STORE),Specs.responseSpecification());
        Order orderReq = new Order(200, 200,
                0, LocalDateTime.now(),"placed",true);
        given()
                .when()
                .header("api_key","special-key")
                .body(orderReq).post("/order")
                .then().statusCode(200);
        given()
                .when().get("/order/200").then()
                .statusCode(200);
    }
    @DisplayName("Проверка поиска заказа")
    @Test
    public void getOrderById(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_STORE),Specs.responseSpecification());
        Order orderReq = new Order(200, 200,
                0, LocalDateTime.now(),"placed",true);
        given()
                .when()
                .header("api_key","special-key")
                .body(orderReq).post("/order")
                .then().statusCode(200);
        given()
                .when().get("/order/200").then()
                .statusCode(200);
    }
    @DisplayName("Проверка удаления заказа")
    @Test
    public void deleteOrderById(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_STORE),Specs.responseSpecification());
        Order orderReq = new Order(200, 200,
                0, LocalDateTime.now(),"placed",true);
        given()
                .when()
                .header("api_key","special-key")
                .body(orderReq).post("/order")
                .then().statusCode(200);
        given()
                .when().get("/order/200").then()
                .statusCode(200);
        given()
                .when()
                .header("api_key","special-key")
                .body(orderReq).delete("/order/"+orderReq.id)
                .then().statusCode(200);
        given()
                .when().get("/order/200").then()
                .statusCode(404);

    }
    @DisplayName("Проверка ошибки удаления заказа")
    @Test
    public void failDeleteOrderById(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_STORE),Specs.responseSpecification());
        Order orderReq = new Order(200, 200,
                0, LocalDateTime.now(),"placed",true);
        given()
                .when()
                .header("api_key","special-key")
                .body(orderReq).post("/order")
                .then().statusCode(200);
        given()
                .when().get("/order/200").then()
                .statusCode(200);
        given()
                .when()
                .header("api_key","special-key")
                .body(orderReq).delete("/order/-"+orderReq.id)
                .then().statusCode(404);
        given()
                .when().get("/order/200").then()
                .statusCode(200);

    }
    @Test
    @DisplayName("Проверка вставки пользователя")
    public void postUser(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_USER),
                Specs.responseSpecification());

        User userReq = new User(200, "Nick",
                "Melisa", "Popovich","a@b.com",
                "123","990-22-33",0);
        given().when().
                header("api_key","special-key")
                .delete("/"+userReq.username);
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(404).body("message",equalTo("User not found"));
        given()
                .when()
                .header("api_key","special-key")
                .body(userReq).post()
                .then().statusCode(200);
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(200).body("username",equalTo(userReq.getUsername()));
    }
    @Test
    @DisplayName("Проверка вставки группы пользователей")
    public void postUsers(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_USER),
                Specs.responseSpecification());

        User userReq = new User(202, "Nicky",
                "Melisa", "Popovich","a@b.com",
                "123","990-22-33",0);
        User userReq2 = new User(201, "Nickel",
                "Melisa", "Popovich","a@b.com",
                "123","990-22-33",0);
        ArrayList<User> users = new ArrayList<>(3);
        users.add(userReq);
        users.add(userReq2);

        given()
                .when()
                .header("api_key","special-key")
                .body(users).post("/createWithList")
                .then().statusCode(200)
        ;
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(200).body("username",equalTo(userReq.getUsername()));
        given()
                .when().get("/"+userReq2.getUsername()).then()
                .statusCode(200).body("username",equalTo(userReq2.getUsername()));
    }
    @Test
    @DisplayName("Проверка удаления пользователя")
    public void deleteUser(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_USER),
                Specs.responseSpecification());

        User userReq = new User(200, "Nick",
                "Melisa", "Popovich","a@b.com",
                "123","990-22-33",0);
        given()
                .when()
                .header("api_key","special-key")
                .body(userReq).post()
                .then().statusCode(200)
        ;
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(200).body("username",equalTo(userReq.getUsername()));
        given().when().
                header("api_key","special-key")
                .delete("/"+userReq.username)
                .then().statusCode(200);
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(404).body("message",equalTo("User not found"));
    }
    @Test
    @DisplayName("Проверка получения пользователя")
    public void getUser(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_USER),
                Specs.responseSpecification());

        User userReq = new User(200, "Nick",
                "Melisa", "Popovich","a@b.com",
                "123","990-22-33",0);
        given()
                .when()
                .header("api_key","special-key")
                .body(userReq).post()
                .then().statusCode(200)
        ;
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(200).body("username",equalTo(userReq.getUsername()));
    }
    @Test
    @DisplayName("Проверка обновления пользователя")
    public void updateUser(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_USER),
                Specs.responseSpecification());

        User userReq = new User(200, "Nick",
                "Melisa", "Popovich","a@b.com",
                "123","990-22-33",0);
        User userReq2 = new User(200, "Nick",
                "Melisasa", "Popovich","a@b.com",
                "123","990-22-32",0);
        given()
                .when()
                .header("api_key","special-key")
                .body(userReq).post()
                .then().statusCode(200)
        ;
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(200).body("username",equalTo(userReq.getUsername()));
        given()
                .when()
                .header("api_key","special-key")
                .body(userReq2).put("/"+userReq.username)
                .then().statusCode(200)
        ;
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(200).body("firstName",equalTo(userReq2.getFirstName()));
    }
    @Test
    @DisplayName("Проверка прохождения логина пользователя")
    public void loginUser(){
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_USER),
                Specs.responseSpecification());
        given()
                .when()
                .header("api_key","special-key")
                .get("/logout")
                .then().statusCode(200);
        User userReq = new User(200, "Nick",
                "Melisa", "Popovich","a@b.com",
                "123","990-22-33",0);

        given()
                .when()
                .header("api_key","special-key")
                .body(userReq).post()
                .then().statusCode(200);
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(200).body("username",equalTo(userReq.getUsername()));

        given()
                .when()
                .header("api_key","special-key")
                .queryParam("username ",userReq.username)
                .queryParam("password ",userReq.password)
                .get("/login")
                .then().statusCode(200);

    }
    @Test
    @DisplayName("Проверка провала логина пользователя")
    public void failLoginUser(){
        //этот тест должен проваливаться
        Specs.installSpec(Specs.requestSpecification(BASE_URL,BASE_USER),
                Specs.responseSpecification());

        User userReq = new User(201, "Nickel",
                "Melisa", "Popovich","a@b.com",
                "123","990-22-33",0);

        given()
                .when()
                .header("api_key","special-key")
                .body(userReq).post()
                .then().statusCode(200);
        given()
                .when().get("/"+userReq.getUsername()).then()
                .statusCode(200).body("username",equalTo(userReq.getUsername()));
        given()
                .when()
                .header("api_key","special-key")
                .get("/logout")
                .then().statusCode(200);
        given()
                .when()
                .header("api_key","special-key")
                .queryParam("username ",userReq.username)
                .queryParam("password ",userReq.password+"1")
                .get("/login")
                .then().statusCode(200);

    }
}
