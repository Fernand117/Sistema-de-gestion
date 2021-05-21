<!doctype html>
<html lang="{{ app()->getLocale() }}">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>EL BUEN TONO - API</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-+0n0xVW2eSR5OomGNYDnhzAbDsOXxcvSN1TPprVMTNDbiYZCxYbOOl7+AMvyTG2x" crossorigin="anonymous">
        <!-- Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Raleway:100,600" rel="stylesheet" type="text/css">

        <!-- Styles -->
        <style>
            *{
                margin: 0;
                padding: 0;
            }
            body {
                font-family: 'Courier New', Courier, monospace;
                background-image: url("http://www.serverfer.com/sistemaAPI/img/mechanical-key_2K.jpg");
            }

            header{
                width: 100%;
                text-align: center;
                background-color: royalblue;
                color: white;
            }

            .content {
                margin: 0 auto;
                width: 90%;
                background-color: red;
                padding: 2%;
                color: white;
                margin-top: 8%;
                text-align: center;
            }

            .doc {
                width: 90%;
                height: 50vh;
                margin: 0 auto;
                margin-top: 2%;
                overflow: scroll;
                text-align: center;
                color: white;
                background-color: royalblue;
            }

            .contentDoc{
                width: 100%;
                background-color: rebeccapurple;
                height: 100vh;
            }
        </style>
    </head>
    <body>
        <header>
            <h1>EL BUEN TONO - API</h1>
        </header>

        <div class="content">
            BIENVENIDO AL SERVIDOR DE EL BUEN TONO, QUEDA PROHÍBIDO SU ACCESO A PERSONAS NO AUTORIZADAS, CUALQUIER TIPO DE ACTIVIDAD DENTRO DEL MISMO ESTÁ SIENDO MONITOREADA POR I.A
        </div>

        <div class="doc">

            <h1>DOCUMENTACIÓN DEL FUNCIONAMIENTO DEL API</h1>
            <div class="contentDoc"></div>
        </div>
    </body>
</html>
