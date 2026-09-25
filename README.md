# Liga BetPlay Fantasy — Prueba de API-Football

Proyecto Spring Boot (Java 21) para probar `https://v3.football.api-sports.io`
antes de construir la app tipo Fantasy sobre la Liga BetPlay colombiana.

## 1. Configurar la API key

```bash
export API_FOOTBALL_KEY=tu_key_real
```

> Si tu suscripción es vía **RapidAPI** en lugar de directa en api-sports.io,
> edita `RestClientConfig.java` y cambia el header `x-apisports-key` por
> `x-rapidapi-key` + `x-rapidapi-host: v3.football.api-sports.io` (está
> comentado en el archivo, solo hay que descomentarlo).

## 2. Ejecutar

```bash
cd liga-betplay-fantasy-test
mvn spring-boot:run
```

La app queda en `http://localhost:8080`.

## 3. Flujo de prueba (en orden)

### Paso 1 — Test de la API e identificar el ID de Liga BetPlay

```
GET http://localhost:8080/api/leagues/search?name=Betplay
```

o filtrando por país:

```
GET http://localhost:8080/api/leagues/search?country=Colombia
```

Busca en la respuesta el objeto cuyo `league.name` sea **"Primera A"** (así
la nombra API-Football; comercialmente es la Liga BetPlay) y copia su
`league.id`. Ese ID ya viene precargado como referencia en
`application.yml` (`api-football.liga-betplay-id: 239`), pero **verifícalo
tú mismo** con esta llamada porque puede variar.

### Paso 2 — Consultar los partidos (fixtures)

Usando el ID detectado (o el que trae por defecto):

```
GET http://localhost:8080/api/fixtures?season=2026
GET http://localhost:8080/api/fixtures?leagueId=239&season=2026
```

De la respuesta, copia el `fixture.id` del partido que te interese.

### Paso 3 — Estadísticas individuales de jugadores de un partido

```
GET http://localhost:8080/api/fixtures/{fixtureId}/players
```

Esto devuelve, por equipo, cada jugador con sus estadísticas del partido
(minutos jugados, goles, asistencias, pases, duelos, tarjetas, rating, etc.),
que es la data base para tu motor de puntuación Fantasy.

## Notas para cuando pases de "prueba" a app Fantasy real

- **Rate limits**: el plan gratis de API-Football tiene un límite bajo de
  requests/día. Para una app real conviene cachear (Redis o una tabla local)
  las respuestas de `fixtures` y `players` en vez de llamarlas en cada
  request del usuario.
- **Temporada**: el parámetro `season` en API-Football espera el año en que
  arranca la temporada (por ejemplo, `2026`), no un rango.
- **DTOs tipados**: este proyecto devuelve `JsonNode` crudo a propósito, para
  que puedas explorar la forma real de la respuesta primero. Cuando pases a
  producción conviene crear DTOs (`FixtureResponse`, `PlayerStatsResponse`,
  etc.) con Jackson para tener tipado fuerte y mapear a tu propio modelo de
  puntuación Fantasy.
- **Manejo de errores**: falta manejo explícito de errores HTTP (401 si la
  key es inválida, 429 si te pasas del rate limit). Vale la pena agregar un
  `@ControllerAdvice` antes de seguir construyendo sobre esto.
