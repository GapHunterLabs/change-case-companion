# Demo — Change Case Companion

1. Abrí `demo/sample.txt` en el sandbox.
2. Seleccioná uno de los identificadores de ejemplo (click+arrastrar,
   o `Home` luego `Shift+End` para agarrar toda la línea y despues
   ajustar). Para `userProfileSettings` doble-click alcanza (es una
   sola "palabra" para el editor).
3. Click derecho → **Change Case Companion** → probá las 5 opciones:
   - Convert to camelCase
   - Convert to PascalCase
   - Convert to snake_case
   - Convert to kebab-case
   - Convert to CONSTANT_CASE
4. Repetí con cada uno de los 5 identificadores de ejemplo (así se
   prueban las 5 conversiones partiendo de cada estilo de origen, no
   solo camelCase → algo).
5. Probá también los "edge cases" al final del archivo:
   - `http2Server` (dígitos pegados a una letra minúscula)
   - `parseXMLDocument` (siglas en mayúscula seguidas)
   - `ID` (todo mayúsculas, 2 letras)
   - `a` (una sola letra)
   - `already_snake_case_with_numbers123`

## Qué reportar

- ¿El resultado de cada conversión es el esperado?
- ¿Algún caso raro rompe algo (deja texto corrupto, no hace nada, o
  tira una excepción)?
- ¿Funciona igual bien en otro tipo de archivo (probá pegar una de las
  líneas en un .java o .md y convertir ahí también)?
