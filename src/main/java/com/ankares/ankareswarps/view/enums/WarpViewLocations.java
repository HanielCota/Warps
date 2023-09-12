package com.ankares.ankareswarps.view.enums;

import com.ankares.ankareswarps.view.SkullIcons;
import lombok.Getter;

@Getter
public enum WarpViewLocations {
    FARM(
            "farm",
            SkullIcons.FARM_SKULL,
            "§7Semeie, plante e colha, a vida no campo é aduar mas satisfatória.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar."),
    MINA(
            "mina",
            SkullIcons.MINA_SKULL,
            "§7Explore, recolha e tome muito cuidado, ",
            "§7as minas são locais perigosos para aqueles que não tem experiência.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar."),
    SHOP(
            "shop",
            SkullIcons.SHOP_SKULL,
            "§7Barganha, preços baixos e descontos,",
            "§7tudo aquilo que queremos em uma boa compra.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar."),
    CEU(
            "céu",
            SkullIcons.CEU_SKULL,
            "§7Seres estranhos vivem lá,",
            "§7há um conhecimento oculto que necessita de uma linguagem não conhecida.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar."),
    ARENA(
            "arena",
            SkullIcons.ARENA_SKULL,
            "§7Os melhores duelistas passaram por aqui",
            "§7e deixaram seu legado, venha e sinta a fúria e ira dos seus oponentes.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar."),
    PORTAIS(
            "portais",
            SkullIcons.PORTAIS_SKULL,
            "§7Aqueles do céu tinham razão,",
            "§7essa dimensão esconde segredos e línguas ocultas.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar."),
    POCO(
            "poço",
            SkullIcons.POCO_SKULL,
            "§7O seu desejo pode ser realizado,",
            "§7ou apenas sofrerá uma punição por sua ganância.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar."),
    PESCA(
            "pesca",
            SkullIcons.PESCA_SKULL,
            "§7Aventurar-se em alto mar requer conhecimento,",
            "§7estude sobre os peixes e desfrute de boas viagens e recompensas.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar."),
    FLORESTA(
            "floresta",
            SkullIcons.FLORESTA_SKULL,
            "§7O vento sussurrando através das árvores",
            "§7a imensidão e calmaria da floresta podem ser traiçoeiras.",
            "",
            "§fClique com o §eSHIFT + CLICK ESQUERDO §fpara adicionar.");

    private final String locationName;
    private final String skullIcon;
    private final String[] lore;

    WarpViewLocations(String locationName, String skullIcon, String... lore) {
        this.locationName = locationName;
        this.skullIcon = skullIcon;
        this.lore = lore;
    }
}
