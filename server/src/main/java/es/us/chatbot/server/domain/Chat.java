package es.us.chatbot.server.domain;



import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A Chat.
 */
@Entity
@Table(name = "chat")
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "context")
    private String context;

    @Column(name = "first_message_date")
    private LocalDateTime firstMessageDate;

    @Column(name = "last_message_date")
    private LocalDateTime lastMessageDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public Chat context(String context) {
        this.context = context;
        return this;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public LocalDateTime getFirstMessageDate() {
        return firstMessageDate;
    }

    public Chat firstMessageDate(LocalDateTime firstMessageDate) {
        this.firstMessageDate = firstMessageDate;
        return this;
    }

    public void setFirstMessageDate(LocalDateTime firstMessageDate) {
        this.firstMessageDate = firstMessageDate;
    }

    public LocalDateTime getLastMessageDate() {
        return lastMessageDate;
    }

    public Chat lastMessageDate(LocalDateTime lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
        return this;
    }

    public void setLastMessageDate(LocalDateTime lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Chat)) {
            return false;
        }
        return id != null && id.equals(((Chat) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Chat{" +
            "id=" + getId() +
            ", context='" + getContext() + "'" +
            ", firstMessageDate='" + getFirstMessageDate() + "'" +
            ", lastMessageDate='" + getLastMessageDate() + "'" +
            "}";
    }
}
