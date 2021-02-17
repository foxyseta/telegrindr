package bot;

import bot.data.Filter;
import bot.data.Profile;
import bot.data.Stat;

import java.util.function.Consumer;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * A <code>TeleGrindr</code> object stores its creator ID. It also defines many
 * abilities.
 *
 * @see Ability
 * @author FoxySeta
 * @version 1.0.0
 * @since 1.0.0
 */
public class TeleGrindr extends AbilityBot {

    /**
     * Creates a new instance of <code>Telegrind</code>.
     * 
     * @param botToken Bot token sent by FatherBot.
     * @param username Username communicated to FatherBot.
     * @param creatorId Your telegram account identifier.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0 
     */
    public TeleGrindr(String botToken, String username, int creatorId) {
        super(botToken, username);
        cId = creatorId;
    }

    /**
     * Gets the bot's creator identifier.
     * 
     * @see #cId
     * @return The bot creator's unique identifier.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    @Override
    public int creatorId() {
        return cId;
    }

    /**
     * Gets the ability triggered by <code>/start</code>.
     * 
     * @return The ability triggered by <code>/start</code>
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    public Ability start() {
        return Ability
                .builder()
                .name("start")
                .info("short introduction")
                .input(0)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(startAction)
                .enableStats()
                .build();
    }
    
    /**
     * Gets the ability triggered by <code>/help</code>.
     * 
     * @return The ability triggered by <code>/help</code>.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    public Ability help() {
        return Ability
                .builder()
                .name("help")
                .info("prints a short guide")
                .input(0)
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .action(helpAction)
                .enableStats()
                .build();
    }

    /**
     * Gets the ability triggered by <code>/iam</code>.
     * 
     * @return The ability triggered by <code>/iam</code>.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    public Ability iam() {
        return Ability
                .builder()
                .name("iam")
                .info("edits your profile")
                .input(0)
                .locality(Locality.GROUP)
                .privacy(Privacy.PUBLIC)
                .action(iamAction)
                .reply(iamReply, Flag.LOCATION)
                .enableStats()
                .build();
    }
    
    /**
     * Gets the ability triggered by <code>/howis</code>
     * 
     * @return The ability triggered by <code>/howis</code>.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    public Ability howis() {
        return Ability
                .builder()
                .name("howis")
                .info("displays a profile")
                .input(1)
                .locality(Locality.GROUP)
                .privacy(Privacy.PUBLIC)
                .action(howisAction)
                .enableStats()
                .build();
    }

    /**
     * Gets the ability triggered by <code>/whois</code>
     * 
     * @return The ability triggered by <code>/whois</code>.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    public Ability whois() {
        return Ability
                .builder()
                .name("whois")
                .info("searches for profiles")
                .input(0)
                .locality(Locality.GROUP)
                .privacy(Privacy.PUBLIC)
                .action(whoisAction)
                .enableStats()
                .build();
    }

    /**
     * Sends a message containing a single {@link Profile}.
     * 
     * @param p The {@link Profile} to print.
     * @param chatId The identifier of the chat to use.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    public void print(Profile p, Long chatId) {
        silent.sendMd(p.toString(), chatId);
        if (p.location != null) {
            SendLocation location = new SendLocation();
            location.setChatId(chatId.toString());
            location.setHorizontalAccuracy(p.location.getHorizontalAccuracy());
            location.setLatitude(p.location.getLatitude());
            location.setLongitude(p.location.getLongitude());
            try {
                execute(location);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a message listing a sequence where each element is a {@link
     * Profile}.
     *
     * @param profiles The profiles to be listes.
     * @param chatId The identifier of the chat to use.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    public void print(Profile[] profiles, Long chatId) {
        silent.send(String.format(PEOPLECOUNTER, profiles.length)
                    + Stream.of(profiles).map(p -> p.toShortString())
                       .collect(Collectors.joining(String.format("%n"),
                                                   String.format("%n"), "")),
                    chatId);
    }

    /** The creator's unique identifier. */
    private int cId;
    /** The prefix used when tagging a certain user. */
    final private static char TAGPREFIX = '@';
    /** The prefix used when removing a tag from your own {@link Profile}. */
    final private static String REMOVETAGARGUMENTPREFIX = "-";
    /** The regex for any tag action to be applied to a {@link Profile}. */
    final private static String TAGARGUMENTREGEX = "([+-]?)#([0-9A-Za-z]+).*";
    /** The regex representing a {@link Stat} and its value. */
    final private static String STATARGUMENTREGEX = "(\\d+)(\\w+)";
    /** The name format for {@link Profile} tables. */
    final private static String PROFILESTABLE = "Profiles_%d";
    /** The message format to be used on unrecognized arguments. */
    final private static String UNKNOWNARGUMENT = "%s❓";
    /** The label representing the concept of a {@link Location}. */
    final private static String LOCATIONLABEL = "📍";
    /** The format of any counter used for {@link Profile} instances. */
    final private static String PEOPLECOUNTER = "👤 × %d";
    /**
     * The {@link Pattern} generated from the {@linkplain #TAGARGUMENTREGEX tag
     * actions' regex}.
     */
    final private static Pattern TAGARGUMENTPATTERN =
        Pattern.compile(TAGARGUMENTREGEX);
    /**
     * The {@link Pattern} generated from the {@linkplain #STATARGUMENTREGEX
     * stat-value pairs' regex}.
     */
    final private static Pattern STATARGUMENTPATTERN =
        Pattern.compile(STATARGUMENTREGEX);
        
    /**
     * Retrieves a {@link Profile} from the database.
     * 
     * @param chatId The chat where the {@link Profile} was set up.
     * @param user The {@link User} who set up the {@link Profile}.
     * @return The {@Profile} in question.
     */
    private Profile getProfile(Long chatId, User user) {
        final Map<Integer, Profile> profiles =
            db.getMap(String.format(PROFILESTABLE, chatId));
        final Integer userId = user.getId();
        if (profiles.containsKey(userId)) {
            final Profile oldProfile = profiles.get(userId);
            oldProfile.user = user;
            return oldProfile;
        }
        final Profile newProfile = new Profile(user);
        profiles.put(user.getId(), newProfile);
        return newProfile;
    }

    /**
     * Updates (or adds if absent) a {@link Profile} in the database.
     * 
     * @param chatId The chat where the {@link Profile} was set up.
     * @param profile The up-to-date {@link Profile}.
     * @return The out-of-date {@link Profile} or <code>null</code>.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    private Profile setProfile(Long chatId, Profile profile) {
        return db.<Integer, Profile>getMap(String.format(PROFILESTABLE, chatId))
                 .put(profile.user.getId(), profile);
    }

    /**
     * Applies a certain action to a {@link Profile}.
     * 
     * @param profile The {@Profile} to be edited.
     * @param argument A {@String} representing the action to apply.
     * @return <code>true</code> on success, <code>false</code> on failure.
     * @author FoxySeta
     * @version 1.0
     * @since 1.0
     */
    private boolean update(Profile profile, String argument) {
        Matcher matcher = TAGARGUMENTPATTERN.matcher(argument);
        if (matcher.matches()) {
            String tag = matcher.group(2);
            if (matcher.group(1).equals(REMOVETAGARGUMENTPREFIX))
                profile.removeTag(tag);
            else
                profile.addTag(tag);
        } else {
            matcher = STATARGUMENTPATTERN.matcher(argument);
            if (matcher.matches()) {
                final String uom = matcher.group(2);
                for (Stat stat : Stat.values())
                    if (stat.uom().equals(uom)) {
                        final int value = Integer.parseInt(matcher.group(1));
                        if (stat.validate(value))
                            profile.putStat(stat, value);
                        break;
                    }
            } else {
                matcher = Profile.EMOJIPATTERN.matcher(argument);
                if (matcher.matches())
                    profile.setEmoji(argument);
                else
                    return false;
            }
        }
        return true;
    }

    /** The action related to the <code>/start</code> command. */
    final private Consumer<MessageContext> startAction = ctx-> {
        silent.sendMd(String.format(
            "*Hi!* 👋%nThe name's _TeleGrindr_ and I can help you get in contact " +
            "with other users in the same Telegram groups as you. If you do " +
            "not know where to start, just ask me for /help."
        ), ctx.chatId());
    };
    
    /** The action related to the <code>/help</code> command. */
    final private Consumer<MessageContext> helpAction = ctx -> {
        final Long chat = ctx.chatId();
        silent.sendMd(String.format(
            "::= = is%n" +
            "| = or%n" +
            "\\[ ] = once or none%n" +
            "{ } = zero or more times"), chat);
        silent.sendMd(String.format(
            "*👤 SETTING UP YOUR PROFILE%n" +
            "/iam {argument}*%n%n" + 
            "_argument_ ::= _stat_|_property_%n" +
            "_stat_ ::= _integer_(yo|cm|kg)%n" +
            "_property_ ::= \\[+|-]#_hashtag_%n%n" +
            "ex. `/iam 29yo -#jock #nerd 175cm` sets your age and height. " +
            "It also replaces one of your tags with another.%n" +
            "📍 You can also send me your location."), chat);
        silent.sendMd(String.format(
            "*🔍 SHOWING SOMEONE'S PROFILE%n" +
            "/howis @%s*", getBotUsername()), chat);
        silent.sendMd(String.format(
            "*👥 LISTING PROFILES%n" +
            "/whois {filter}*%n%n" +
            "_filter_ ::= _range_|_property_%n" +
            "_range_ ::= \\[_integer_]\\[,]\\[_integer_](yo|cm|kg|km)%n" +
            "_property_ ::= \\[+|-]#_hashtag_%n%n" +
            "ex. `/whois 18,29yo #single ,10km -#sporty` selects profiles " +
            "within the specified ranges and (not) having the specified tags."
        ), chat);
    };

    /** The action related to the <code>/iam</code> command. */
    final private Consumer<MessageContext> iamAction = ctx -> {
        final Long chat = ctx.chatId();
        final Profile profile = getProfile(ctx.chatId(), ctx.user());
        for (String argument : ctx.arguments())     
            if (!update(profile, argument))
                silent.send(String.format(UNKNOWNARGUMENT, argument),
                            ctx.chatId());
        print(profile, chat);
        setProfile(chat, profile);
    };
    
    /** The reply related to the <code>/iam</code> command. */
    final private Consumer<Update> iamReply = upd -> {
        final Message message = upd.getMessage();
        final Profile profile = getProfile(upd.getMessage().getChatId(),
                                           upd.getMessage().getFrom());
        profile.location = message.getLocation();
        setProfile(message.getChatId(), profile);
        print(profile, upd.getMessage().getChatId());
    };

    /** The action related to the <code>/howis</code> command. */
    final private Consumer<MessageContext> howisAction = ctx -> {
        final String arg = ctx.firstArg(),
                     tag = arg.substring(arg.charAt(0) == TAGPREFIX ? 1 : 0);
        final Long chat = ctx.chatId();
        Optional<Profile> profile = db
            .<Integer, Profile>getMap(String.format(PROFILESTABLE, chat))
            .values().stream().filter(
                p -> p != null && p.user != null &&
                     tag.equalsIgnoreCase(p.user.getUserName())
            ).findAny();
        if (profile.isPresent())
            print(profile.get(), chat);
        else
            silent.send(String.format(UNKNOWNARGUMENT, TAGPREFIX + tag), chat);
    };

    /** The action related to the <code>/whois</code> command. */
    final private Consumer<MessageContext> whoisAction = ctx -> {
        final Long chat = ctx.chatId();
        final Location from = getProfile(chat, ctx.user()).location;
        final Filter filter = new Filter(ctx.arguments(), from);
        if (from != null || !filter.isLocationNeeded())
            print(db
                .<Integer, Profile>getMap(String.format(PROFILESTABLE, chat))
                .values().stream().filter(filter)
                .toArray(Profile[]::new), chat);
        else
            silent.send(String.format(UNKNOWNARGUMENT, LOCATIONLABEL), chat);
    };

}