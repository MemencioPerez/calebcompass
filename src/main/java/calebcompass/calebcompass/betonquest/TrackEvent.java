package calebcompass.calebcompass.betonquest;

import calebcompass.calebcompass.util.CompassInstance;
import org.betonquest.betonquest.api.profiles.OnlineProfile;
import org.betonquest.betonquest.api.profiles.Profile;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.betonquest.betonquest.Instruction;
import org.betonquest.betonquest.api.QuestEvent;
import org.betonquest.betonquest.exceptions.InstructionParseException;
import org.betonquest.betonquest.exceptions.QuestRuntimeException;
import org.betonquest.betonquest.utils.location.CompoundLocation;

public class TrackEvent extends QuestEvent {

	private CompoundLocation trackLoc;

	public TrackEvent(Instruction instruction) throws InstructionParseException {
		super(instruction, false);
		trackLoc = instruction.getLocation();
	}

	@Override
	protected Void execute(Profile profile) throws QuestRuntimeException {

		Location tracked = trackLoc.getLocation(profile);
		OnlineProfile onlineProfile = profile.getOnlineProfile().get();
		Player player = onlineProfile.getPlayer();

		if (CompassInstance.getInstance().getCompassLocation(player) == null) {
			CompassInstance.getInstance().addCompassLocation(player, player.getLocation(), tracked);
		}

		CompassInstance.getInstance().getCompassLocation(player).setOrigin(player.getLocation());
		CompassInstance.getInstance().getCompassLocation(player).setTarget(tracked);
		CompassInstance.getInstance().getCompassLocation(player).setTracking(true);

		CompassInstance.getInstance().saveData();
		return null;
	}

}