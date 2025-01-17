package calebcompass.calebcompass.betonquest;

import calebcompass.calebcompass.SavePoints.SavePointConfig;
import calebcompass.calebcompass.util.CompassInstance;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.api.profiles.Profile;
import org.bukkit.entity.Player;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;

public class TogglePoint extends QuestEvent {

    private boolean toToggle;
    private String name;

    public TogglePoint(Instruction instruction) throws InstructionParseException {
        super(instruction, false);
        name = instruction.getPart(1);
        if (!SavePointConfig.getInstance().pointExistsExplicit(name)) throw new InstructionParseException("Enter a valid waypoint");
        String ins = instruction.getPart(2);
        if (ins.equalsIgnoreCase("enable")) toToggle = true;
        if (ins.equalsIgnoreCase("disable")) toToggle = false;
        if (!ins.equalsIgnoreCase("disable") && !ins.equalsIgnoreCase("enable")) throw new InstructionParseException("Enter either enable/disable");

    }

    @Override
    protected Void execute(Profile profile) throws QuestRuntimeException {
        OnlineProfile onlineProfile = profile.getOnlineProfile().get();
        Player player = onlineProfile.getPlayer();
        SavePointConfig.getInstance().togglePlayerPoint(player.getUniqueId(), name, toToggle);

        return null;
    }

}