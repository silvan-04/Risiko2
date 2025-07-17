package Risiko.commands;

import java.io.Serial;
import java.io.Serializable;

public class GameCommand implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final GameCommandType type;
    private final Object payload;

    public GameCommand(GameCommandType type) {
        this.type = type;
        this.payload = null;
    }

    public GameCommand(GameCommandType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public GameCommandType getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }
}

