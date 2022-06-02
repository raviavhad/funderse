# FUNDERSE - Decentralized Consumer Credit

## Inspiration

To begin, there is **BNPL services are becoming increasingly popular around the world**, with more than a third of US customers having used them in some form. First and foremost, according to the BNPL provider split, the largest difficulty is how to recoup lost ecommerce sales. Third, the growing popularity of younger consumers and those with thin credit histories.

##What it does
We provide an innovative approach to financing here. Our solution is called Funderse, which **drives the decentralized consumer credit. The concept is to introduce cryptocurrency into the BNPL realm. Cryptocurrency-accepting systems should be supported using the BNPL approach, just as the TradFi** world is.

Our solution utilizes the features of **NFTs to enrich our solution further digitizing and securing the rewards and offers for the customers**. Lets take a look at the overall process.

Customers use the Funderse platforms for their transactions online or offline by scanning QR code of the merchants or retailers. One the Funderse transaction completes and the NFT Tokens as rewards are credited to customers wallet for future use. Customer can either use those NFT tokens or trade them online to amplify the gains from his transaction. This service allows for greater transparency and security of buying, selling, and lending of physical goods as well as digital goods and assets. A large majority of NFTs today are digital assets, and we feel this technology is being greatly under-utilized at the moment. With funders we are striving for **optimal integration of BNPL, Chainlink and NFTs for benefits of customer today and in future**.

Funderse is a decentralized exchange protocol that runs on the BNPL platform, which is built on the Chainlink blockchain. Funderse is a forward-thinking platform that aims to share advantages with all participants. We are aiming to create a market for everyday transactions and micropayments using Crypto and BNPL. The platform includes third-party applications, merchants, online retail stores, and the world's most well-known staking mechanism. Introducing the Funderse token, a reward system based on predefined events such as purchase or instalment payment. Funderse brings two distinct sets of capabilities.

Integrated wallets for Chainlink, ETHERUM, and AURORA, as well as relevant defi capabilities such as transaction visibility, wallet transfer, and yield staking. Funderse tokens will be used to integrate the BNPL paradigm for cryptocurrencies. The application displays transactions for each cryptocurrency, including Chainlink, Ethereum, and Aurora. Additionally, the app includes filters and search capabilities for transactions. Transfer enables the exchange of tokens across linked wallets and with Funderse. The Funderse Wallet tab displays a dashboard that displays the available Funderse tokens in the form of a balance, a limit, and spent tokens. This dashboard establishes restrictions on an individual's BNPL credit for ecommerce transactions, whether online or in-person. This way, the customer has the choice of paying using available tokens in their Chainlink, Ethereum, or AURORA wallets or with Funderse tokens via the BNPL channel. In the event of the BNPL option, repayment can be made via a pre-defined EMI. This can be accomplished using the wallet's available currency or their debit or credit cards. Users receive benefits in the form of Funderse tokens following installment. Funderse offers different methods of earning Funderse NFT tokens. The Funderse NFT tokens Points earned will vary between different cards based on transaction categories such as shopping at partner stores, partner restaurants, other dining expenses, entertainment and so on.

## How we built it
This is accomplished through an established technology stack that enables us to operate with the agility, scale, and ease of integration required in an ecosystem. Outsystems is a low-code platform that enables the delivery of experiences to users across many channels – web, mobile Android, and iOS. Node.js is used to construct APIs on top of the Chainlink blockchain, which may be consumed by an ecommerce platform or the Funderse app, depending on the demoed functionalities. Balance and transfer APIs have been developed, as well as an underlying Chainlink API that is based on the Chainlink account ID.
##Challenges we ran into
Initially, bringing numerous wallet coins to Funderse as a unified platform presented some issues. Through Funderse, we were able to integrate Chainlink, Ethereum, and Aurora chains into the solution for the Hackathon. We linked OutSystems' low-code platform with the Chainlink environment to enable API-driven transactions. Initially, we encountered integration challenges, but we were able to integrate our application quickly.

##Accomplishments that we're proud of
Funderse's extensibility allows for the addition of other wallets and interoperability with e-commerce systems such as Amazon, Shopify, and others. Funderse was conceptualised and implemented as an additional token to complement BNPL's methods of financing in order to target untapped shoppers. Aligned with Web 3.0 standards, with transparency, openness, and scalability at its core

##What we learned
About the world of cryptocurrency and how it may be used in real-world circumstances, particularly in finance, with Funderse

##What's next for Funderse
Establish guidelines to facilitate open integration among ecommerce operators for the purpose of bundling BNPL via Funderse.



## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, Funderse generates configuration files for tools like git, prettier, eslint, husk, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if ommited) or force. Lines starting with `#` are considered comments and are ignored.
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

To start your application in the dev profile, run:

```
./gradlew
```


### Funderse Control Center

Funderse Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker-compose -f src/main/docker/funderse-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the funderse application for production, run:

```
./gradlew -Pprod clean bootJar
```

To ensure everything worked, run:

```
java -jar build/libs/*.jar
```


### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./gradlew -Pprod -Pwar clean bootWar
```

## Testing

To launch your application's tests, run:

```
./gradlew test integrationTest jacocoTestReport
```

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the gradle plugin.

Then, run a Sonar analysis:

```
./gradlew -Pprod clean check jacocoTestReport sonarqube
```


## Using Docker to simplify development (optional)

You can use Docker to improve your Funderse development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mysql database in a docker container, run:

```
docker-compose -f src/main/docker/mysql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/mysql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./gradlew bootJar -Pprod jibDockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```



