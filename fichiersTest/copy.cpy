      * Copy d'exemple pour mes tests
       01 zone-groupe.
         05 premiere-donnee PIC X(05)
            VALUE 'plop.'.
         05 deuxieme-donnee
                            PIC X(03).
         05 donnee-numerique PIC 9(2).
           88 deux VALUE 2.
           88 deouze VALUE 12.
         05 zone-redefine.
           10 grosse-donnee PIC X(10).
         05 premier-groupe REDEFINES zone-redefine.
           10 donnee-un PIC X(04).
           10 donnee-deux PIC 99V99.
         05 deuxieme-groupe REDEFINES zone-redefine.
           10 donnee-un-bis PIC X(05).
           10 donnee-deux-bis PIC X(05).
         05 zone-simple-occurs PIC X OCCURS 3.