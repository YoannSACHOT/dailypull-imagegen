# DailyPull Image Generator

Outil de génération d'images pour les cartes [DailyPull](https://github.com/YoannSACHOT/dailypull) via l'API [1min.ai](https://1min.ai).

## Modèles supportés

| Modèle | ID API |
|--------|--------|
| DALL-E 3 | `dall-e-3` |
| DALL-E 2 | `dall-e-2` |
| GPT Image 1 | `gpt-image-1` |
| Flux Pro 1.1 | `black-forest-labs/flux-1.1-pro` |
| Flux Dev | `black-forest-labs/flux-dev` |
| Flux Schnell | `black-forest-labs/flux-schnell` |
| Stable Image Core | `stable-image` |
| Gemini 3 Pro Image | `gemini-3-pro-image-preview` |
| Gemini 3.1 Flash Image | `gemini-3-1-flash-image-preview` |

## Prérequis

- Java 25+
- Maven 3.9+
- Une clé API [1min.ai](https://app.1min.ai/members)

## Installation

```bash
git clone https://github.com/YoannSACHOT/dailypull-imagegen.git
cd dailypull-imagegen
cp src/main/resources/application-dev.yml.example src/main/resources/application-dev.yml
```

Renseigner la clé API dans `application-dev.yml` :

```yaml
onemin:
  api:
    key: VOTRE_CLE_API
```

## Lancement

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Le serveur démarre sur le port **8090**.

## API

### Générer une image

```bash
curl -X POST http://localhost:8090/api/images/generate \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "A motivational card with golden sunrise, courage theme, elegant typography",
    "model": "DALL_E_3",
    "size": "1024x1024",
    "quality": "hd",
    "style": "vivid"
  }'
```

### Générer un batch

```bash
curl -X POST http://localhost:8090/api/images/generate/batch \
  -H "Content-Type: application/json" \
  -d '{
    "requests": [
      {"prompt": "Courage card — mountain peak at dawn", "model": "DALL_E_3"},
      {"prompt": "Serenity card — calm ocean under moonlight", "model": "FLUX_PRO_1_1"}
    ]
  }'
```

### Lister les modèles disponibles

```bash
curl http://localhost:8090/api/images/models
```

## Paramètres de génération

| Paramètre | Défaut | Options |
|-----------|--------|---------|
| `prompt` | *(requis)* | — |
| `model` | `DALL_E_3` | Voir tableau des modèles |
| `size` | `1024x1024` | `1024x1024`, `1024x1792`, `1792x1024` |
| `quality` | `hd` | `standard`, `hd` |
| `style` | `vivid` | `vivid`, `natural` |
| `outputFormat` | `png` | `png`, `jpeg`, `webp` |

Les images générées sont sauvegardées automatiquement dans `./generated-images/`.

## Stack technique

- Java 25
- Spring Boot 4.0.3
- Jackson 3
- JUnit 5 + Mockito

## Licence

Projet interne [JIXTER](https://github.com/YoannSACHOT).
