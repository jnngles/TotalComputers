package com.jnngl.totalcomputers;

public class Localization {

    private static Localization singleton;

    public static void init(Localization localization) {
        singleton = localization;
    }

    public static String get(int idx) {
        if(singleton == null) return "Undefined locale";
        if(idx < 0 || idx >= singleton.messages.length) return "Invalid message index: "+idx;
        return singleton.messages[idx];
    }

    public static Localization get() {
        return singleton;
    }

    public final String[] messages;

    public Localization(String[] messages) {
        this.messages = messages;
    }

    public static class HeccrbqZpsr extends Localization {

        public HeccrbqZpsr() {
            super(new String[] {
                    "Подключен клиент: ", // 0
                    "Отключен клиент: ", // 1
                    " уже использует захват движений на этом компьютере.", // 2
                    "Включен захват движений!", // 3
                    "Конфигурация: ", // 4
                    "  Движение: ", // 5
                    "  Взгляд: ", // 6
                    "  Прыжок: ", // 7
                    "  Приседание: ", // 8
                    "  Слот: ", // 9
                    "  Дроп: ", // 10
                    "Команда для выхода: ","", // 11,12
                    "Да", // 13
                    "Нет", // 14
                    " уже использует захват движений на этом компьютере.", // 15
                    "Захват движений отключен.", // 16
                    "Помощь", // 17
                    "Алиасы: ", // 18
                    " - этот список", // 19
                    " - создаёт ссылку для использования аудио", // 20
                    " - создаёт компьютер", // 21
                    " - удаляет компьютер", // 22
                    " - список компьютеров", // 23
                    " - информация о компьютере", // 24
                    " - включает/выключает/переключает/выводит возможность выделения области", // 25
                    " - даёт инструмент для выделения области", // 26
                    " - вставляет текст в ближайший компьютер", // 27
                    " - удаляет текст с ближайший компьютера", // 28
                    " - перезагружает конфигурацию плагина", // 29
                    " - сбрасывает токен", // 30
                    " - выводит токен для клиента TotalComputers", // 31
                    " - информация о клиенте TotalComputers", // 32
                    " - привязывает компьютер к клиенту", // 33
                    " - отвязывает компьютер от клиента", // 34
                    "В скором времени эта команда будет удалена! Вместо этого используйте discord бота!", // 35
                    "Ссылка: ", // 36
                    "У вас недостаточно прав!", // 37
                    "Конфигурация перезагружена", // 38
                    "Все компьютера были перезагружены", // 39
                    "Что-то пошло не так!", // 40
                    "Заметка: Чтобы перезагрузить бота и настройки сервера TotalComputers нужно перезапустить сервер", // 41
                    "У вас недостаточно прав!", // 42
                    "Текст не может быть пустым", // 43
                    "У вас недостаточно прав!", // 44
                    "`","' - не число", // 45,46
                    "У вас недостаточно прав!", // 47
                    "Выделение области включено", // 48
                    "Выделение области выключено", // 49
                    "Выделение области ","включено","выключено", // 50,51,52
                    "Сейчас выделение области","включено","выключено", // 53,54,55
                    "У вас недостаточно прав!", // 56
                    "Вы уже поставили все компьютеры!", // 57
                    "Вы не выбрали область или она не правильная", // 58
                    "Соотношение сторон должно быть как минимум 4:3", // 59
                    "Ширина компьютера не может быть больше чем 16 блоков", // 60
                    "Высота компьютера не может быть больше чем 9 блоков", // 61
                    "Ширина компьютера не может быть меньше чем 4 блока", // 62
                    "Высота компьютера не может быть меньше чем 3 блока", // 63
                    "Компьютер не может быть размещен на горизонтальной поверхности", // 64
                    "Компьютер с таким названием уже существует", // 65
                    "Компьютер с названием '","' был создан", // 66, 67
                    "Компьютеры привязанные к клиенту выключены на этом сервере, вам нужно привязать этот компьютер к клиенту", // 68
                    "Введите `/tcmp client' для более подробной информации", // 69
                    "Название компьютера не может содержать пробелы", // 70
                    "Сервер TotalComputers отключен на этом сервере :(", // 71
                    "Компьютеры привязанные к клиенту отключены на этом сервере", // 72
                    "У вас недостаточно прав!", // 73
                    "Не существует компьютера с названием `","`.", // 74, 75
                    "Нужно сгенерировать токен и подключить клиент перед использованием этой команды", // 76
                    "Отправка запроса... (это может занять некоторое время)", // 77
                    "Запрос уже отправлен", // 78
                    "Превышено время ожидания ответа", // 79
                    "Токен не найден. (Подключен ли клиент?)", // 80
                    "Этот компьютер уже привязан к клиенту", // 81
                    "Не существует такого компьютера привязанного к клиенту", // 82
                    "Компьютер отвязан от клиента", // 83
                    "1. Скачайте клиент TotalComputers: ", // 84
                    "2. Сгенерируйте токен с помощью /tcmp token", // 85
                    "3. Запустите клиент и подключитесь к серверу", // 86
                    "(Порт: ",")", // 87, 88
                    "4. Введите `tcmp client bind <название>' для того чтобы привязать компьютер к клиенту", // 89
                    "Введите `/tcmp help' для других команд", // 90
                    "У вас недостаточно прав!", // 91
                    "Не существует компьютера с названием '","'.", // 92,93
                    "Компьютер с названием '","' удален", // 94,95
                    "Название компьютера не может содержать пробелов", // 96
                    "У вас недостаточно прав!", // 97
                    "Никаких.", // 98
                    "Доступные компьютеры: ", // 99
                    "Ваш токен: ", // 100
                    "У вас недостаточно прав!", // 101
                    "Не существует компьютера с названием '","'.", // 102,103
                    "Информация:", // 104
                    "Название: ", // 105
                    "Первая угол: ", // 106
                    "   X: ", // 107
                    "   Y: ", // 108
                    "   Z: ", // 109
                    "Второй угол: ", // 110
                    "   X: ", // 111
                    "   Y: ", // 112
                    "   Z: ", // 113
                    "Ось: ", // 114
                    "Направление: ", // 115
                    "Ширина: ", // 116
                    "Высота: ", // 117
                    "Площадь: ", // 118
                    "Мир: ", // 119
                    "Название компьютера не может содержать пробелов", // 120
                    "Только игроки могут выполнять эту команду.", // 121
                    "Компьютер", // 122
                    "Теперь вы можете создать свой компьютер :D", // 123
                    "1. /tcmp wand", // 124
                    "2. Выделите область", // 125
                    "3. /tcmp create <название>", // 126
                    "Первая позиция установлена. (X: ",", Y: ",", Z: ",")", // 127,128,129,130
                    "Вторая позиция установлена. (X: ",", Y: ",", Z: ",")", // 131,132,133,134
                    "Вы слишком далеко от компьютера", // 135
                    "Сначала откройте компьютер", // 136
                    "У вас недостаточно прав!", // 137
                    "Вы не можете использовать компьютер с предметом в руке!", // 138
                    "Неправильное использование команды! Введите '/tcmp help' для информации о командах.", // 139
                    "Позиции находятся в разных мирах", // 140
                    "Выделенная область должна быть плоской", // 141
                    "Выделенная область не может быть 1*1.", // 142
                    "Выделенная область: [Направление: ",", площадь: "," (","*",")]", // 143,144,145,146,147
                    "У меня недостаточно прав =(", // 148
                    "Вы не подключены к голосовому каналу", // 149
                    "Я уже подключен!", // 150
                    "Ну... Я не подключен!", // 151
                    "Запущен компьютер привязанный к клиенту", // 152
                    "Выберете область", // 153
                    "Нет свободных токенов :(", // 154
                    "Не удалось проверить наличие обновлений", // 155
                    "Доступно обновление: ", // 156
                    "Ссылка на скачивание: ", // 157
                    "Пожалуйста подождите несколько секунд...", // 158
            });

        }

    }

    public static class EnglishLang extends Localization {

        public EnglishLang() {
            super(new String[] {
                    "Connected ", // 0
                    "Disconnected ", // 1
                    " is already using this feature on this computer.", // 2
                    "You are now using motion capture!", // 3
                    "Configuration: ", // 4
                    "  Movement: ", // 5
                    "  Gaze: ", // 6
                    "  Jump: ", // 7
                    "  Sneak: ", // 8
                    "  Slot: ", // 9
                    "  Item Drop: ", // 10
                    "Type "," to stop it", // 11,12
                    "Yes", // 13
                    "No", // 14
                    " is already using this feature on this computer.", // 15
                    "You are no longer using the capture feature.", // 16
                    "Help", // 17
                    "Aliases: ", // 18
                    " - show help menu.", // 19
                    " - creates a link to access audio.", // 20
                    " - creates new computer at area of wall selected with wand. (See also /totalcomputers selection)", // 21
                    " - removes computer.", // 22
                    " - prints list of created computers.", // 23
                    " - prints information about computer.", // 24
                    " - enables/disables/toggles/prints possibility of wall area selection with wand.", // 25
                    " - gives wand", // 26
                    " - pastes text. (Keyboard alternative)", // 27
                    " - erases text. (Keyboard alternative)", // 28
                    " - reloads all configuration files.", // 29
                    " - resets TotalComputers client token.", // 30
                    " - prints your TotalComputers client token.", // 31
                    " - info about TotalComputers client.", // 32
                    " - binds computers to client.", // 33
                    " - unbinds clientbound computer.", // 34
                    "This subcommand is deprecated and highly not recommended to use! Use discord bot instead", // 35
                    "Open this website in your browser: ", // 36
                    "You do not have enough permissions!", // 37
                    "Configuration files has been successfully reloaded!", // 38
                    "All computers has been successfully reloaded.", // 39
                    "Something went wrong!", // 40
                    "Note: To reload discord bot and server settings you need to restart the server", // 41
                    "You don't have enough permissions!", // 42
                    "Nothing to paste.", // 43
                    "You don't have enough permissions!", // 44
                    "`","' is not a number.", // 45,46
                    "You do not have enough permissions!", // 47
                    "Selection using wand successfully enabled.", // 48
                    "Selection using wand successfully disabled.", // 49
                    "Selection using wand successfully ","enabled","disabled", // 50,51,52
                    "Selection using wand is currently ","enabled","disabled", // 53,54,55
                    "You do not have enough permissions!", // 56
                    "You have placed all your computers!", // 57
                    "You have not selected area or it is invalid. Select it with wand. Make sure area selection is enabled.", // 58
                    "Aspect ratio should be at least 4:3", // 59
                    "Maximum width in blocks is 16.", // 60
                    "Maximum height in blocks is 9.", // 61
                    "Minimum width in blocks is 4.", // 62
                    "Minimum height in blocks is 3.", // 63
                    "Computer cannot be placed on horizontal surface.", // 64
                    "Computer with this name already exists.", // 65
                    "Computer with name '","' successfully created!", // 66,67
                    "Serverbound computers are disabled on this server, you need to bind this computer to client", // 68
                    "Type `/tcmp client' for more info", // 69
                    "Computer name cannot contain spaces!", // 70
                    "TotalComputers server is disabled on this server :(", // 71
                    "Clientbound computers are disabled on this server", // 72
                    "You do not have enough permissions!", // 73
                    "There is no such computer with name '","'.", // 74,75
                    "You should generate token and connect client before running this command.", // 76
                    "Sending request... (This may take some time)", // 77
                    "Request has already been sent.", // 78
                    "Timed out.", // 79
                    "Invalid token. (Is there any connected clients?)", // 80
                    "This computer is already clientbound.", // 81
                    "There is no such clientbound computer.", // 82
                    "Destroyed clientbound computer.", // 83
                    "1. Download TotalComputers client: ", // 84
                    "2. Generate token with /tcmp token", // 85
                    "3. Run client and connect to this server", // 86
                    "(Server port: ",")", // 87,88
                    "4. Use `/tcmp client bind <name>' command to bind computer to your client", // 89
                    "Type `/tcmp help' to more commands", // 90
                    "You do not have enough permissions!", // 91
                    "There is no such computer with name '","'.", // 92,93
                    "Computer with name '","' successfully removed!", // 94,95
                    "Computer name cannot contain spaces!", // 96
                    "You do not have enough permissions!", // 97
                    "None.", // 98
                    "Available computers: ", // 99
                    "Your token is ", // 100
                    "You do not have enough permissions!", // 101
                    "There is no such computer with name '","'.", // 102,103
                    "Information:", // 104
                    "Name: ", // 105
                    "First Corner Position: ", // 106
                    "   X: ", // 107
                    "   Y: ", // 108
                    "   Z: ", // 109
                    "Second Corner Position: ", // 110
                    "   X: ", // 111
                    "   Y: ", // 112
                    "   Z: ", // 113
                    "Axis: ", // 114
                    "Facing Direction: ", // 115
                    "Width: ", // 116
                    "Height: ", // 117
                    "Area: ", // 118
                    "World: ", // 119
                    "Computer name cannot contain spaces!", // 120
                    "Only players can execute this command.", // 121
                    "Computer", // 122
                    "Now you can create your own computer :D", // 123
                    "1. /tcmp wand", // 124
                    "2. Select area", // 125
                    "3. /tcmp create <name>", // 126
                    "First position is set. (X: ",", Y: ",", Z: ",")", // 127,128,129,130
                    "Second position is set. (X: ",", Y: ",", Z: ",")", // 131,132,133,134
                    "You are too far from the computer", // 135
                    "Open keyboard first", // 136
                    "You don't have enough permissions!", // 137
                    "You are trying to use the computer with an item in main hand!", // 138
                    "Invalid usage! Use '/totalcomputers help' for information about commands.", // 139
                    "Positions are located in different worlds!", // 140
                    "Selected area should be flat!", // 141
                    "Selected area cannot be 1*1.", // 142
                    "Selected Area: [Direction: ",", Area: "," (","*",")]", // 143,144,145,146,147
                    "I don't have permissions to do that =(", // 148
                    "You are not connected to a voice channel", // 149
                    "Already connected!", // 150
                    "Uhh... I'm not connected!", // 151
                    "Started clientbound computer.", // 152
                    "Select area", // 153
                    "There is no free tokens :(", // 154
                    "Unable to check for updates", // 155
                    "An update is available: ", // 156
                    "Download link: ", // 157
                    "Please wait some time...", // 158
            });
        }
    }

}
