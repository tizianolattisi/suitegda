# -*- mode: python-mode; coding: utf-8 -*-
# :Progetto:  PyPaPi SuitePA - estrazione codici stato
# :Creato:    mer 21 dicembre 2011
# :Autore:    Tiziano Lattisi <tiziano@axiastudio.it>
# :Licenza:   GNU General Public License version 3 or later
#

TEMPLATE = "INSERT INTO anagrafiche.stato (id, codice, descrizione) VALUES (%d, '%s', '%s');"

f = open("codici_stato.txt", "r")
content = f.read()
f.close()

rows = content.split("\n")
rows.sort()

i = 0
for row in rows:
	i += 1
	tkns = row.split(" ")
	codice = tkns[0].strip()
	descrizione = " ".join(tkns[1:]).strip().replace("'", "''")
	print TEMPLATE % (i, codice, descrizione)
	