      * Copy d'exemple pour mes tests
       01 zone-groupe.
         05 premiere-donnee PIC X(05)
            VALUE 'plop.'.
         05 deuxieme-donnee
                            PIC X(03).
         05 donnee-numerique PIC 9(2).
           88 deux VALUE 2.
           88 deouze VALUE 12.
         05 grosse-donnee PIC X(10).
         05 premier-groupe REDEFINES grosse-donnee.
           10 donnee-un PIC X(04).
           10 donnee-deux PIC X(16).
         05 deuxieme-groupe REDEFINES grosse-donnee.
           10 donnee-un-bis PIC X(05).
           10 donnee-deux-bis PIC X(05).