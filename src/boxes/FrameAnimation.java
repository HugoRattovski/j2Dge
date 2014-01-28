package boxes;

import java.util.HashMap;

public class FrameAnimation {

    public HashMap<EnumFrameSequenceId, FrameSequence> Sequences;

    public FrameSequence getSequence(EnumFrameSequenceId id) {
        FrameSequence tmpSeq = this.Sequences.get(id);
        if (tmpSeq != null) {
            return tmpSeq;
        }
        else {
            return this.Sequences.get(EnumFrameSequenceId.SEQ_IDLE);
        }
    }
}
