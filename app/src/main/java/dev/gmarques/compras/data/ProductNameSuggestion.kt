package dev.gmarques.compras.data

import dev.gmarques.compras.domain.utils.ExtFun.Companion.removeAccents
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class ProductNameSuggestion {

    private var suggestions = arrayListOf<String>()

    /**
     * Retorna uma lista de sugestões de nomes de produtos que contenham o termo fornecido.
     * @param term Termo a ser buscado.
     * @return Lista de sugestões contendo o termo.
     */
    suspend fun getSuggestion(term: String): List<String> {
        if (suggestions.isEmpty()) loadSuggestions()
        return suggestions.filter { it.removeAccents().contains(term.removeAccents(), ignoreCase = true) }
    }

    private suspend fun loadSuggestions() = withContext(IO) {
        suggestions.addAll(
            arrayListOf(
                "Maçã 🍎",
                "Pera 🍐",
                "Laranja 🍊",
                "Limão 🍋",
                "Banana 🍌",
                "Melancia 🍉",
                "Uva 🍇",
                "Morango 🍓",
                "Manga 🥭",
                "Abacaxi 🍍",
                "Kiwi 🥝",
                "Coco 🥥",
                "Pêssego 🍑",
                "Cereja 🍒",
                "Melão 🍈",
                "Amora 🫐",
                "Framboesa 🫐",
                "Mirtilo 🫐",
                "Acerola 🌿",
                "Jabuticaba 🌿",
                "Pitanga 🌿",
                "Caju 🌿",
                "Tamarindo 🌿",
                "Maracujá 🌿",
                "Goiaba 🌿",
                "Graviola 🌿",
                "Romã 🌿",
                "Abacate 🥑",
                "Lichia 🌿",
                "Carambola 🌿",
                "Sapoti 🌿",
                "Figo 🌿",
                "Caqui 🌿",
                "Fruta-do-conde 🌿",
                "Cupuaçu 🌿",
                "Bacuri 🌿",
                "Camu-camu 🌿",
                "Physalis 🌿",
                "Rambutã 🌿",
                "Mangostão 🌿",
                "Durian 🌿",
                "Longan 🌿",
                "Salak 🌿",
                "Abóbora 🎃",
                "Abobrinha 🥒",
                "Aipim 🌿",
                "Alho 🧄",
                "Amaranto 🌾",
                "Amêndoa 🌰",
                "Amendoim 🥜",
                "Andu 🌿",
                "Arachachá 🌿",
                "Arroz 🌾",
                "Arroz-selvagem 🌾",
                "Aveia 🌾",
                "Avelã 🌰",
                "Azeitona 🫒",
                "Açafrão 🌿",
                "Acelga 🥬",
                "Acelga-chinesa 🥬",
                "Agave-caribenho 🌿",
                "Agrião 🌱",
                "Aipo 🌿",
                "Alcachofra 🌼",
                "Alcaparra 🌿",
                "Alecrim 🌿",
                "Alface 🥬",
                "Alfafa 🌿",
                "Alho-poró 🌾",
                "Almeirão 🌱",
                "Aspargo 🌿",
                "Azedinha 🌿",
                "Baroa 🥕",
                "Batata 🥔",
                "Batata doce 🍠",
                "Berinjela 🍆",
                "Beterraba 🌿",
                "Brócolis 🥦",
                "Castanha-do-pará 🌰",
                "Caxi 🌿",
                "Cebola 🧅",
                "Cenoura 🥕",
                "Centeio 🌾",
                "Cevada 🌾",
                "Chalota 🌿",
                "Chia 🌾",
                "Chuchu 🥒",
                "Couve-nabo 🌿",
                "Couve-rábano 🌿",
                "Cumaru 🌿",
                "Cambuquira 🌿",
                "Capeba 🌿",
                "Capiçoba 🌿",
                "Capuchinha 🌸",
                "Caruru 🌿",
                "Catalonha 🌿",
                "Cebolinha 🌱",
                "Chaya 🌿",
                "Chicória 🌿",
                "Coentro 🌿",
                "Comelina 🌿",
                "Couve 🥬",
                "Couve-de-bruxelas 🥬",
                "Couve-flor 🌸",
                "Dendê 🌿",
                "Dill 🌿",
                "Ervilha 🌱",
                "Ervilha-de-pombo 🌿",
                "Echalota 🌿",
                "Embaúba 🌿",
                "Endívia 🌿",
                "Erva-cidreira 🌿",
                "Erva-doce 🌿",
                "Erva-mate 🌿",
                "Espinafre 🌿",
                "Estragão 🌿",
                "Fava 🌱",
                "Fruta-pão 🌿",
                "Funcho 🌿",
                "Gengibre 🌿",
                "Gergelim 🌾",
                "Girassol 🌻",
                "Gobô 🌿",
                "Goya 🌿",
                "Grão-de-bico 🌿",
                "Grumixama 🌿",
                "Guandu 🌿",
                "Guasca 🌿",
                "Hibisco 🌺",
                "Hortelã 🌿",
                "Inajá 🌿",
                "Inhame 🌿",
                "Jalapeño 🌶️",
                "Jicama 🌿",
                "Jiló 🍆",
                "Lentilha 🌾",
                "Linhaça 🌾",
                "Louro 🌿",
                "Mandioca 🌿",
                "Mandioquinha 🌿",
                "Maxixe 🌿",
                "Mendubiguaçu 🌿",
                "Milho 🌽",
                "Mitsubá 🌿",
                "Mogango 🌿",
                "Monguba 🌿",
                "Moranga 🎃",
                "Madressilva 🌸",
                "Major-gomes 🌿",
                "Manjericão 🌿",
                "Manjerona 🌿",
                "Maria-gorda 🌿",
                "Mostarda 🌱",
                "Nabo 🌿",
                "Noz 🌰",
                "Oliva 🫒",
                "Ora-pro-nobis 🌿",
                "Orégano 🌿",
                "Orelha-de-coelho 🌿",
                "Palmito 🌴",
                "Paru 🌿",
                "Pau-rei 🌿",
                "Pé-de-anta 🌿",
                "Pelega 🌿",
                "Pepino 🥒",
                "Pequi 🌿",
                "Pimenta 🌶️",
                "Pinhão 🌰",
                "Pistache 🌰",
                "Peixinho-da-horta 🌿",
                "Palma 🌿",
                "Picão 🌿",
                "Quiabo 🌿",
                "Quinoa 🌾",
                "Quina 🌿",
                "Rabanete 🌿",
                "Rábano 🌿",
                "Raiz-forte 🌿",
                "Rutabaga 🌿",
                "Radicchio 🌿",
                "Repolho 🥬",
                "Rúcula 🌿",
                "Ruibarbo 🌿",
                "Shissô 🌿",
                "Soja 🌿",
                "Sorgo 🌾",
                "Salsa 🌿",
                "Salsão 🌿",
                "Sálvia 🌿",
                "Serralha 🌿",
                "Taioba 🌿",
                "Taro 🌿",
                "Tomate 🍅",
                "Tonka 🌿",
                "Trigo 🌾",
                "Tupinambo 🌿",
                "Tingensai 🌿",
                "Tomilho 🌿",
                "Trapoeraba 🌿",
                "Urucum 🌿",
                "Urtiga 🌿",
                "Vagem 🌱",
                "Vinagreira 🌿",
                "Wasabi 🌿",
                "Xixá 🌿",
                "Xoconostle 🌿",
                "Zizânia 🌾",
                "Abiu 🌿",
                "Abricó 🌿",
                "Abrunho 🌿",
                "Açaí 🫐",
                "Acerola 🍒",
                "Akee 🌿",
                "Alfarroba 🌿",
                "Ameixa 🍑",
                "Amora 🍇",
                "Ananás 🍍",
                "Anona 🌿",
                "Araçá 🌿",
                "Arando 🫐",
                "Araticum 🌿",
                "Ata 🌿",
                "Atemoia 🌿",
                "Babaco 🌿",
                "Babaçu 🌿",
                "Bacaba 🌿",
                "Bacupari 🌿",
                "Baru 🌰",
                "Bergamota 🍊",
                "Biribá 🌿",
                "Buriti 🌿",
                "Butiá 🌿",
                "Cabeludinha 🌿",
                "Cacau 🌰",
                "Cagaita 🌿",
                "Caimito 🌿",
                "Cajá 🌿",
                "Caju 🍎",
                "Calabaça 🌿",
                "Calabura 🌿",
                "Calamondin 🍋",
                "Cambucá 🌿",
                "Cambuci 🌿",
                "Caqui 🍅",
                "Carambola ⭐",
                "Carnaúba 🌿",
                "Castanha 🌰",
                "Ciriguela 🌿",
                "Ciruela 🌿",
                "Cranberry 🫐",
                "Damasco 🍑",
                "Dekopon 🍊",
                "Dióspiro 🌿",
                "Dovyalis 🌿",
                "Durião 🌿",
                "Embaubarana 🌿",
                "Engkala 🌿",
                "Escropari 🌿",
                "Esfregadinha 🌿",
                "Esporão-de-galo 🌿",
                "Framboesa 🍓",
                "Figo-da-índia 🌿",
                "Gabiroba 🌿",
                "Glicosmis 🌿",
                "Goiaba 🍐",
                "Granadilla 🌿",
                "Gravatá 🌿",
                "Groselha 🌿",
                "Guabiju 🌿",
                "Guabiroba 🌿",
                "Guaraná 🌿",
                "Hawthorn 🌿",
                "Jabuticaba 🍇",
                "Jaca 🌴",
                "Jambo 🌿",
                "Jambolão 🌿",
                "Jamelão 🌿",
                "Jaracatiá 🌿",
                "Jatobá 🌿",
                "Jenipapo 🌿",
                "Jerivá 🌿",
                "Juá 🌿",
                "Jujuba 🌿",
                "Maçã 🍎",
                "Macadâmia 🌰",
                "Mamão 🍈",
                "Mamoncillo 🌿",
                "Mangaba 🌿",
                "Maracujá 🍈",
                "Marmelo 🌿",
                "Mexerica 🍊",
                "Murici 🌿",
                "Nectarina 🍑",
                "Oxicoco 🌿",
                "Pitanga 🍒",
                "Pitaia 🌺",
                "Pitomba 🌿",
                "Pomelo 🍊",
                "Pupunha 🌿",
                "Pajurá 🌿",
                "Romã 🍎",
                "Rambutão 🌿",
                "Seriguela 🌿",
                "Tangerina 🍊",
                "Tâmara 🌿",
                "Condicionador 🧴",
                "Sabonete líquido 🧴",
                "Creme dental 🪥",
                "Escova de dentes 🪥",
                "Fio dental 🧵",
                "Enxaguante bucal 🫗",
                "Desodorante 🧴",
                "Sabonete íntimo 🧴",
                "Papel higiênico 🧻",
                "Lenços umedecidos 🧻",
                "Algodão 💊",
                "Cotonetes 👂",
                "Creme para barbear 🧴",
                "Lâmina de barbear 🪒",
                "Gilete 🪒",
                "Esponja de aço 🧽✨",
                "Bom bril 🧽✨",
                "Detergente 🧴🧽",
                "Prestobarba 🪒",
                "Gel de barbear 🧴",
                "Aparelho de depilação 🪒",
                "Hidratante 🧴",
                "Protetor solar 🌞",
                "Perfume 🌸",
                "Máscara facial 🧖‍♀️",
                "Escova de cabelo 💇‍♀️",
                "Pente 💇‍♂️",
                "Fraldas 🩲",
                "Absorventes 🩸",
                "Esfoliante corporal 🧴",
                "Sabonete 🧼",
                "Shampoo 💆‍♂️",
                "Máscara capilar 💆‍♀️",
                "Óleo corporal 🌿",
                "Cortador de unha ✂️",
                "Creme para as mãos ✋",
                "Batom 💄",
                "Base de maquiagem 🖌️",
                "Máscara de cílios 👁️",
                "Corretivo ✨",
                "Espelho 🪞",
                "Lenços demaquilantes 🧻",
                "Removedor de maquiagem 🧴",
                "Desinfetante 🧴",
                "Limpador multiuso 🧴",
                "Amaciante de roupas 🧴",
                "Alvejante 🧴",
                "Desinfetante de banheiro 🚽",
                "Desengordurante 🧴",
                "Esponja de cozinha 🧽",
                "Cloro 🧴",
                "Álcool 70% 🍶",
                "Sabão em barra 🧼",
                "Sabão em pó 🧼",
                "Sabão líquido 🧴🧼",
                "Inseticida 🪲",
                "Removedor de manchas 🧴",
                "Papel toalha 🧻",
                "Luvas de limpeza 🧤",
                "Limpador de vidro 🧴",
                "Desodorizador de ambiente 🌸",
                "Vassoura 🧹",
                "Pá de lixo 🗑️",
                "Sacos de lixo 🗑️",
                "Limpador de móveis 🪑",
                "Limpador de carpetes 🧴",
                "Água 💧",
                "Refrigerante 🥤",
                "Limonada 🍋",
                "Chá gelado 🍹",
                "Chá verde 🍵",
                "Chá preto 🍵",
                "Chá de hibisco 🌺",
                "Chá de camomila 🌼",
                "Chá mate 🍵",
                "Leite 🥛",
                "Leite condensado 🍼",
                "Leite de coco 🥥",
                "Leite de amêndoas 🌰",
                "Café ☕",
                "Cappuccino ☕",
                "Chocolate quente 🍫",
                "Milkshake 🧋",
                "Vinho 🍷",
                "Cerveja 🍺",
                "Whisky 🥃",
                "Vodka 🍸",
                "Rum 🍹",
                "Tequila 🌵",
                "Gin 🍸",
                "Cachaça 🥃",
                "Coquetel 🍸",
                "Margarita 🍹",
                "Licor 🍸",
                "Energético ⚡",
                "Isotônico 🏃‍♂️",
                "Hidromel 🍯",
                "Suco 🍹",
                "Smoothie 🍓🥭",
                "Arroz 🍚",
                "Feijão preto 🫘",
                "Feijão marrom 🫘",
                "Lentilha 🥣",
                "Grão-de-bico 🧆",
                "Aveia 🥣",
                "Soja 🌱",
                "Ervilha 🟢",
                "Pão francês 🥖",
                "Pão de forma 🍞",
                "Bolo 🎂",
                "Donut 🍩",
                "Queijo 🧀",
                "Iogurte 🥛",
                "Manteiga 🧈",
                "Requeijão 🧀",
                "Creme de leite 🥛",
                "Doce de leite 🍯",
                "Coalhada 🧀",
                "Queijo cottage 🧀",
                "Leite de coco 🥥🥛",
                "Queijo minas 🧀",
                "Ricota 🧀",
                "Queijo parmesão 🧀",
                "Alcatra 🥩",
                "Picanha 🥩",
                "Maminha 🥩",
                "Fraldinha 🥩",
                "Chã de dentro 🥩",
                "Chã de fora 🥩",
                "Ovos 🥚",
                "Nugget 🐔",
                "Coxinha da asa 🐔",
                "Lasanha 🍛",
                "Miojo 🍜",
                "Contrafilé 🥩",
                "Filé mignon 🥩",
                "Costela 🥩",
                "Cupim 🥩",
                "Coxão duro 🥩",
                "Coxão mole 🥩",
                "Patinho 🥩",
                "Lagarto 🥩",
                "Acém 🥩",
                "Peito 🥩",
                "Paleta 🥩",
                "Bife de fígado 🥩",
                "Carne de sol 🥩",
                "Carne moída 🥩",
                "Linguiça 🥩🌭",
                "Hambúrguer 🥩🍔",
                "Bisteca 🥩",
                "Músculo 🥩",
                "Rabada 🥩",
                "Charque 🥩",
                "Carne defumada 🥩",
                "Frango 🍗",
                "Coxa de Frango 🍗",
                "Linguiça de frango 🌭🍗",
                "Salsicha de frango 🌭🍗",
                "Almôndega de frango 🍗🍝",
                "Kafta de frango 🍗",
                "Frango à milanesa 🍗🍞",
                "Lombo 🐖",
                "Costelinha 🐖🍖",
                "Pernil 🐖",
                "Linguiça de porco 🌭🐖",
                "Bacon 🥓",
                "Pancetta 🐖",
                "Almôndega 🍝",
                "Açúcar 🍬",
                "Amido de milho 🌽",
                "Caldo Knorr 🧆",
                "Cominho 🌿",
                "Curry 🍛",
                "Gergelim 🍚",
                "Mel 🍯",
                "Molho de alho 🧄🍶",
                "Molho de soja 🍶",
                "Molho inglês 🍶",
                "Sal 🧂",
                "Sal Grosso 🧂",
                "Shoyu 🍶",
                "Vinagre 🍇",
                "Açúcar refinado 🍬",
                "Canela 🍂",
                "Cebolinha 🌿",
                "Fubá 🌽",
                "Farinha de aveia 🌾",
                "Farinha de mandioca 🥔",
                "Farinha de milho 🌽",
                "Farinha de trigo 🌾",
                "Polvilho azedo 🌾",
                "Polvilho doce 🌾",
                "Tapioca 🌾",
                "Alfajor 🍪🍫",
                "Amendoim doce 🥜🍬",
                "Biscoito 🍪",
                "Brigadeiro 🍬",
                "Calda de chocolate 🍫",
                "Caramelo 🍬",
                "Chocolate 🍫",
                "Doce de leite 🥛",
                "Gelatina 🍓",
                "Jujuba 🍬",
                "Marshmallow 🍡",
                "Maçã do amor 🍏🍬",
                "Pé de moleque 🥜",
                "Picolé 🍦",
                "Pudim 🍮",
                "Quindim 🍮",
                "Sorvete 🍦",
                "Trufa 🍫🍬",
                "Wafer 🍪",
                "Açaí 🍇",
                "Algodão doce 🍭",
                "Bolo de rolo 🍰",
                "Churros 🍩",
                "Iogurte congelado 🍦",
                "Abridor de garrafas 🍾",
                "Abridor de latas 🥫",
                "Assadeira 🍽️",
                "Bacia 🛁",
                "Balde 🪣",
                "Batedeira 🍰",
                "Caixa organizadora 📦",
                "Cesto de roupa 🧺",
                "Coador de café ☕",
                "Colher de pau 🥄",
                "Colher medidora 🥄",
                "Concha 🍲",
                "Espátula 🍳",
                "Escorredor de arroz 🍚",
                "Escorredor de macarrão 🍝",
                "Macarrão parafuso 🍝",
                "Macarrão espaguete 🍝",
                "Macarrão talharim🍝",
                "Espremedor de alho 🧄",
                "Espremedor de frutas 🍋",
                "Grelha 🍖",
                "Jogo de panelas 🍳🍲",
                "Jogo de talheres 🍴",
                "Jogo de xícaras ☕",
                "Lixeira 🗑️",
                "Luvas de cozinha 🧤",
                "Moedor de pimenta 🌶️",
                "Molheira 🍯",
                "Panela 🍳",
                "Peneira 🍲",
                "Pote 🫙",
                "Prato 🍽️",
                "Ralador 🧀",
                "Rodo 🧹",
                "Saboneteira 🧼",
                "Saca-rolhas 🍷",
                "Tigela 🍲",
                "Tábua de corte 🔪",
                "Torradeira 🍞",
                "Vassoura 🧹"
            )
        )
    }


}