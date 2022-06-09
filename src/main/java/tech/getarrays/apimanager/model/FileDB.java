package tech.getarrays.apimanager.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
@Getter
@Setter
@Entity
@Table(name="file_db",uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_id")
})
public class FileDB {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type;

    @OneToOne(cascade = CascadeType.MERGE,fetch =FetchType.EAGER)
    @JoinColumn(name="user_id",referencedColumnName ="id")
    private User user;


    @Lob
    private byte[] data;
    public FileDB() {
    }
    public FileDB(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }
}
