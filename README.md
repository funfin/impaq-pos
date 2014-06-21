Sample task for Java Developer candidate

Implement a simple point of sale (PoS).

Assume you have:

● one input device: bar codes scanner

● two output devices: LCD display and printer

Implement:

● single product sale: products bar code is scanned and:

○ if the product is found in products database then it's name and price is printed on

LCD display

    ○ if the product is not found then error message 'Product not found' is printed on

    LCD display

    ○ if the code scanned is empty then error message 'Invalid bar­code' is printed on

    LCD display

    ● when 'exit' is input than receipt is printed on printer containing a list of all previously

    scanned items names and prices as well as total sum to be paid for all items; the total

    sum is also printed on LCD display

Rules:

● use only SDK classes and your favorite test libraries

● mock/stub the database and IO devices

● concentrate on proper design and clean code, rather than supplying fully functioning

application

● if necessary ­ describe your assumptions in dedicated file

Solution:

● full solution should be put on Github / Bitbucket repository and proper link should be sent

to IMPAQ