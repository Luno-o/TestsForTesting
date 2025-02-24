package restservicestests.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import restservicestests.utils.DateSerialazer;

import java.time.LocalDateTime;

public class Order{
    public Integer id;
    public Integer petId;
    public Integer quantity;
    @JsonFormat(shape =  JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    @JsonDeserialize(using = DateSerialazer.class)
    public LocalDateTime shipDate;
    public String status;
    public boolean complete;
    public Order(){
    }

    public Order(int id, int petId, int quantity, LocalDateTime shipDate, String status, boolean complete) {
        this.id = id;
        this.petId = petId;
        this.quantity = quantity;
        this.shipDate = shipDate;
        this.status = status;
        this.complete = complete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getShipDate() {
        return shipDate;
    }

    public void setShipDate(LocalDateTime shipDate) {
        this.shipDate = shipDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
