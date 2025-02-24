package restservicestests.pojo;

import java.util.ArrayList;

public class PetReq {
    private Integer id;
    private Category category;
    private String name;
    private ArrayList<String> photoUrls;
    private ArrayList<Tag> tags;
    private String status;
    public PetReq(Integer id,Category category,
                  String name,ArrayList<String> photoUrls,
                  ArrayList<Tag> tags,
                  String status){
        this.category = category;
        this.id = id;
        this.name = name;
        this.tags = tags;
        this.status = status;
        this.photoUrls = photoUrls;
    }
    public PetReq(){

    };

    public Integer getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getPhotoUrls() {
        return photoUrls;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

