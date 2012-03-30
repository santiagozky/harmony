-- phpMyAdmin SQL Dump
-- version 2.11.3deb1ubuntu1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 01. Juli 2008 um 19:00
-- Server Version: 5.0.51
-- PHP-Version: 5.2.4-2ubuntu5.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Datenbank: `Phosphorus`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Connections`
--

DROP TABLE IF EXISTS `Connections`;
CREATE TABLE `Connections` (
  `PK_Connections` bigint(20) unsigned NOT NULL auto_increment,
  `connectionID` int(11) unsigned NOT NULL,
  `FK_Service` bigint(20) unsigned NOT NULL,
  `minBandwidth` int(11) NOT NULL,
  `maxBandwidth` int(11) NOT NULL,
  `maxLatency` int(11) default NULL,
  `directionality` int(11) NOT NULL,
  `dataAmount` int(11) default NULL,
  `FK_StartpointTNA` varchar(15) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`PK_Connections`),
  KEY `FK_Service` (`FK_Service`),
  KEY `FK_StartpointTNA` (`FK_StartpointTNA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Domain`
--

DROP TABLE IF EXISTS `Domain`;
CREATE TABLE `Domain` (
  `name` varchar(40) collate utf8_bin NOT NULL,
  `seqno` int(11) unsigned default NULL,
  `description` varchar(100) collate utf8_bin default NULL,
  `registered` datetime NOT NULL,
  `reservationEPR` varchar(255) collate utf8_bin NOT NULL,
  `topologyEPR` varchar(255) collate utf8_bin default NULL,
  `notificationEPR` varchar(255) collate utf8_bin default NULL,
  `relationship` varchar(20) collate utf8_bin default NULL,
  `avgDelay` int(11) default NULL,
  `maxBW` int(11) default NULL,
  `Disabled` tinyint(1) NOT NULL default '0',
  `Priority` int(11) NOT NULL default '0',
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Endpoint`
--

DROP TABLE IF EXISTS `Endpoint`;
CREATE TABLE `Endpoint` (
  `TNA` varchar(15) collate utf8_bin NOT NULL,
  `name` varchar(40) collate utf8_bin default NULL,
  `description` varchar(100) collate utf8_bin default NULL,
  `fkDomainName` varchar(40) collate utf8_bin NOT NULL,
  `type` int(11) unsigned NOT NULL,
  `bandwidth` int(11) default NULL,
  `Disabled` tinyint(1) NOT NULL default '0',
  `Priority` int(11) NOT NULL default '0',
  PRIMARY KEY  (`TNA`),
  KEY `FK_DomainName` (`fkDomainName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `InterDomainLink`
--

DROP TABLE IF EXISTS `InterDomainLink`;
CREATE TABLE `InterDomainLink` (
  `PK_interdomainlink` bigint(20) unsigned NOT NULL auto_increment,
  `LinkName` varchar(40) collate utf8_bin NOT NULL,
  `LinkCosts` int(11) default NULL,
  `FK_SourceEndpointTNA` varchar(15) collate utf8_bin NOT NULL,
  `SourceDomain` varchar(40) collate utf8_bin NOT NULL,
  `DestinationDomain` varchar(40) collate utf8_bin NOT NULL,
  `Disabled` tinyint(1) NOT NULL default '0',
  `Priority` int(11) NOT NULL default '0',
  PRIMARY KEY  (`PK_interdomainlink`),
  KEY `FK_SourceEndpointTNA` (`FK_SourceEndpointTNA`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=47 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Link`
--

DROP TABLE IF EXISTS `Link`;
CREATE TABLE `Link` (
  `PK_Link` bigint(20) unsigned NOT NULL auto_increment,
  `FK_DestEndpointTNA` varchar(15) collate utf8_bin NOT NULL,
  `FK_SourceEndpointTNA` varchar(15) collate utf8_bin NOT NULL,
  `name` varchar(40) collate utf8_bin default NULL,
  `description` varchar(100) collate utf8_bin default NULL,
  `delay` int(11) default NULL,
  `costs` int(11) default NULL,
  PRIMARY KEY  (`PK_Link`),
  KEY `FK_DestEndpointTNA` (`FK_DestEndpointTNA`),
  KEY `FK_SourceEndpointTNA` (`FK_SourceEndpointTNA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MAP_ConnEndpoint`
--

DROP TABLE IF EXISTS `MAP_ConnEndpoint`;
CREATE TABLE `MAP_ConnEndpoint` (
  `mConnEndpointID` bigint(20) unsigned NOT NULL auto_increment,
  `FK_Connection` bigint(20) unsigned NOT NULL,
  `FK_DestEndpointTNA` varchar(15) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`mConnEndpointID`),
  KEY `FK_DestEndpointTNA` (`FK_DestEndpointTNA`),
  KEY `FK_Connection` (`FK_Connection`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Mapping for ''connections'' to ''connection endpoints''' AUTO_INCREMENT=115 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `MAP_NRPSResvID`
--

DROP TABLE IF EXISTS `MAP_NRPSResvID`;
CREATE TABLE `MAP_NRPSResvID` (
  `PK_NRPSResvID` bigint(20) unsigned NOT NULL auto_increment,
  `FK_reservationID` bigint(20) unsigned NOT NULL,
  `NrpsReservationID` bigint(20) unsigned NOT NULL,
  `FK_domainName` varchar(40) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`PK_NRPSResvID`),
  KEY `FK_reservationID` (`FK_reservationID`),
  KEY `FK_domainName` (`FK_domainName`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=10 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `NrpsConnection`
--

DROP TABLE IF EXISTS `NrpsConnection`;
CREATE TABLE `NrpsConnection` (
  `PK_NrpsConnection` bigint(20) unsigned NOT NULL auto_increment,
  `FK_Connection` bigint(20) unsigned NOT NULL,
  `FK_Domain` varchar(40) collate utf8_bin NOT NULL,
  `FK_Source` varchar(15) collate utf8_bin NOT NULL,
  `FK_Destination` varchar(15) collate utf8_bin NOT NULL,
  `bandwidth` int(11) NOT NULL,
  `directionality` int(11) NOT NULL,
  `latency` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY  (`PK_NrpsConnection`),
  KEY `FK_Connection` (`FK_Connection`),
  KEY `FK_Domain` (`FK_Domain`),
  KEY `FK_Source` (`FK_Source`),
  KEY `FK_Destination` (`FK_Destination`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='Nrps internal connections' AUTO_INCREMENT=37 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Reservation`
--

DROP TABLE IF EXISTS `Reservation`;
CREATE TABLE `Reservation` (
  `reservationID` bigint(20) unsigned NOT NULL auto_increment,
  `consumerURL` varchar(255) collate utf8_bin default NULL,
  `token` varchar(40) collate utf8_bin default NULL,
  `gri` varchar(40) collate utf8_bin default NULL,
  `timeout` datetime default NULL,
  `jobID` bigint(20) default NULL,
  PRIMARY KEY  (`reservationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Service`
--

DROP TABLE IF EXISTS `Service`;
CREATE TABLE `Service` (
  `PK_service` bigint(20) unsigned NOT NULL auto_increment,
  `serviceID` int(11) unsigned NOT NULL,
  `FK_ReservationID` bigint(20) unsigned NOT NULL,
  `startTime` datetime NOT NULL,
  `deadline` datetime default NULL,
  `duration` int(11) default NULL,
  `automaticActivation` tinyint(1) NOT NULL,
  PRIMARY KEY  (`PK_service`),
  KEY `FK_ReservationID` (`FK_ReservationID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=327 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `Subscription`
--

DROP TABLE IF EXISTS `Subscription`;
CREATE TABLE `Subscription` (
  `subscriptionID` bigint(20) unsigned NOT NULL auto_increment,
  `subscriptionTopic` varchar(255) collate utf8_bin NOT NULL,
  `subscriptionEPR` varchar(255) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`subscriptionID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `TNAPrefix`
--

DROP TABLE IF EXISTS `TNAPrefix`;
CREATE TABLE `TNAPrefix` (
  `prefix` varchar(18) collate utf8_bin NOT NULL,
  `FK_domainName` varchar(40) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`prefix`),
  KEY `FK_domainName` (`FK_domainName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `DomSupportedAdaption`
--

DROP TABLE IF EXISTS `DomSupportedAdaption`;
CREATE TABLE `DomSupportedAdaption` (
  `PK_Adaption` bigint(20) unsigned NOT NULL auto_increment,
  `Adaption` varchar(40) collate utf8_bin NOT NULL,
  `FK_domainName` varchar(40) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`PK_Adaption`),
  KEY `FK_domainName` (`FK_domainName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `DomSupportedSwitchMatrix`
--

DROP TABLE IF EXISTS `DomSupportedSwitch`;
CREATE TABLE `DomSupportedSwitch` (
  `PK_Switch` bigint(20) unsigned NOT NULL auto_increment,
  `Switch` varchar(40) collate utf8_bin NOT NULL,
  `FK_domainName` varchar(40) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`PK_Switch`),
  KEY `FK_domainName` (`FK_domainName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `DomSupportedBandwidth`
--

DROP TABLE IF EXISTS `DomSupportedBandwidth`;
CREATE TABLE `DomSupportedBandwidth` (
  `PK_Bandwidth` bigint(20) unsigned NOT NULL auto_increment,
  `Bandwidth` bigint(20) collate utf8_bin NOT NULL,
  `FK_domainName` varchar(40) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`PK_Bandwidth`),
  KEY `FK_domainName` (`FK_domainName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `VIEW_InterDomainLink`
--
CREATE TABLE `VIEW_InterDomainLink` (
`FK_SourceEndpointTNA` varchar(15)
,`FK_DestEndpointTNA` varchar(15)
,`costs` int(11)
,`delay` int(11)
,`name` varchar(40)
,`description` varchar(141)
);
-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `VIEW_ReservationMapping`
--
CREATE TABLE `VIEW_ReservationMapping` (
`reservationID` bigint(20) unsigned
,`jobID` bigint(20)
,`NrpsReservationID` bigint(20) unsigned
,`FK_domainName` varchar(40)
,`PK_service` bigint(20) unsigned
,`serviceID` int(11) unsigned
,`PK_Connections` bigint(20) unsigned
,`connectionID` int(11) unsigned
,`PK_NrpsConnection` bigint(20) unsigned
);
-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `VIEW_ReservationPeriod`
--
CREATE TABLE `VIEW_ReservationPeriod` (
`reservationID` bigint(20) unsigned
,`startTime` datetime
,`duration` int(11)
,`endTime` datetime
);
-- --------------------------------------------------------

--
-- Stellvertreter-Struktur des Views `VIEW_DomainReservationMapping`
--
CREATE TABLE `VIEW_DomainReservationMapping` (
`domainName` varchar(40)
,`reservationID` bigint(20) unsigned
);
-- --------------------------------------------------------

--
-- Struktur des Views `VIEW_InterDomainLink`
--
DROP TABLE IF EXISTS `VIEW_InterDomainLink`;

DROP VIEW IF EXISTS `VIEW_InterDomainLink`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `VIEW_InterDomainLink` AS (select `A1`.`FK_SourceEndpointTNA` AS `FK_SourceEndpointTNA`,`A2`.`FK_SourceEndpointTNA` AS `FK_DestEndpointTNA`,`A1`.`LinkCosts` AS `costs`,`A1`.`LinkCosts` AS `delay`,`A1`.`LinkName` AS `name`,concat(`A1`.`DestinationDomain`,_utf8'-',`A2`.`DestinationDomain`,_utf8' interdomain link "',`A1`.`LinkName`,_utf8'"') AS `description` from (`InterDomainLink` `A1` join `InterDomainLink` `A2` on(((`A1`.`LinkName` = `A2`.`LinkName`) and (`A1`.`SourceDomain` = `A2`.`DestinationDomain`) and (`A2`.`SourceDomain` = `A1`.`DestinationDomain`)))));

-- --------------------------------------------------------

--
-- Struktur des Views `VIEW_ReservationMapping`
--
DROP TABLE IF EXISTS `VIEW_ReservationMapping`;

DROP VIEW IF EXISTS `VIEW_ReservationMapping`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `VIEW_ReservationMapping` AS (select `Reservation`.`reservationID` AS `reservationID`,`Reservation`.`jobID` AS `jobID`,`MAP_NRPSResvID`.`NrpsReservationID` AS `NrpsReservationID`,`MAP_NRPSResvID`.`FK_domainName` AS `FK_domainName`,`Service`.`PK_service` AS `PK_service`,`Service`.`serviceID` AS `serviceID`,`Connections`.`PK_Connections` AS `PK_Connections`,`Connections`.`connectionID` AS `connectionID`,`NrpsConnection`.`PK_NrpsConnection` AS `PK_NrpsConnection` from ((((`MAP_NRPSResvID` join `Reservation`) join `NrpsConnection`) join `Connections`) join `Service`) where ((`MAP_NRPSResvID`.`FK_reservationID` = `Reservation`.`reservationID`) and (`Reservation`.`reservationID` = `Service`.`FK_ReservationID`) and (`Connections`.`FK_Service` = `Service`.`PK_service`) and (`Connections`.`PK_Connections` = `NrpsConnection`.`FK_Connection`)));

-- --------------------------------------------------------

--
-- Struktur des Views `VIEW_ReservationPeriod`
--
DROP TABLE IF EXISTS `VIEW_ReservationPeriod`;

DROP VIEW IF EXISTS `VIEW_ReservationPeriod`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `VIEW_ReservationPeriod` AS (select `Reservation`.`reservationID` AS `reservationID`,`Service`.`startTime` AS `startTime`,`Service`.`duration` AS `duration`,(`Service`.`startTime` + interval `Service`.`duration` second) AS `endTime` from (`Reservation` join `Service`) where (`Service`.`FK_ReservationID` = `Reservation`.`reservationID`));

-- --------------------------------------------------------

--
-- Struktur des Views `VIEW_DomainReservationMapping`
--
DROP TABLE IF EXISTS `VIEW_DomainReservationMapping`;

DROP VIEW IF EXISTS `VIEW_DomainReservationMapping`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `VIEW_DomainReservationMapping` AS (SELECT DISTINCT Endpoint.fkDomainName as domainName, Service.FK_ReservationID as reservationID FROM Endpoint join (Connections, MAP_ConnEndpoint, Service) on (Endpoint.TNA = Connections.FK_StartpointTNA OR Endpoint.TNA = MAP_ConnEndpoint.FK_DestEndpointTNA AND Connections.PK_Connections = MAP_ConnEndpoint.FK_Connection AND Connections.FK_Service = Service.PK_service));
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `Connections`
--
ALTER TABLE `Connections`
  ADD CONSTRAINT `Connections_ibfk_5` FOREIGN KEY (`FK_Service`) REFERENCES `Service` (`PK_service`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Connections_ibfk_6` FOREIGN KEY (`FK_StartpointTNA`) REFERENCES `Endpoint` (`TNA`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `Endpoint`
--
ALTER TABLE `Endpoint`
  ADD CONSTRAINT `Endpoint_ibfk_1` FOREIGN KEY (`fkDomainName`) REFERENCES `Domain` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `InterDomainLink`
--
ALTER TABLE `InterDomainLink`
  ADD CONSTRAINT `InterDomainLink_ibfk_6` FOREIGN KEY (`FK_SourceEndpointTNA`) REFERENCES `Endpoint` (`TNA`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `Link`
--
ALTER TABLE `Link`
  ADD CONSTRAINT `Link_ibfk_3` FOREIGN KEY (`FK_DestEndpointTNA`) REFERENCES `Endpoint` (`TNA`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Link_ibfk_6` FOREIGN KEY (`FK_SourceEndpointTNA`) REFERENCES `Endpoint` (`TNA`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `MAP_ConnEndpoint`
--
ALTER TABLE `MAP_ConnEndpoint`
  ADD CONSTRAINT `MAP_ConnEndpoint_ibfk_1` FOREIGN KEY (`FK_Connection`) REFERENCES `Connections` (`PK_Connections`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `MAP_ConnEndpoint_ibfk_2` FOREIGN KEY (`FK_DestEndpointTNA`) REFERENCES `Endpoint` (`TNA`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `MAP_NRPSResvID`
--
ALTER TABLE `MAP_NRPSResvID`
  ADD CONSTRAINT `MAP_NRPSResvID_ibfk_1` FOREIGN KEY (`FK_reservationID`) REFERENCES `Reservation` (`reservationID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `MAP_NRPSResvID_ibfk_2` FOREIGN KEY (`FK_domainName`) REFERENCES `Domain` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `NrpsConnection`
--
ALTER TABLE `NrpsConnection`
  ADD CONSTRAINT `NrpsConnection_ibfk_10` FOREIGN KEY (`FK_Source`) REFERENCES `Endpoint` (`TNA`),
  ADD CONSTRAINT `NrpsConnection_ibfk_11` FOREIGN KEY (`FK_Destination`) REFERENCES `Endpoint` (`TNA`),
  ADD CONSTRAINT `NrpsConnection_ibfk_8` FOREIGN KEY (`FK_Connection`) REFERENCES `Connections` (`PK_Connections`) ON DELETE CASCADE,
  ADD CONSTRAINT `NrpsConnection_ibfk_9` FOREIGN KEY (`FK_Domain`) REFERENCES `Domain` (`name`);

--
-- Constraints der Tabelle `Service`
--
ALTER TABLE `Service`
  ADD CONSTRAINT `Service_ibfk_1` FOREIGN KEY (`FK_ReservationID`) REFERENCES `Reservation` (`reservationID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `TNAPrefix`
--
ALTER TABLE `TNAPrefix`
  ADD CONSTRAINT `TNAPrefix_ibfk_1` FOREIGN KEY (`FK_domainName`) REFERENCES `Domain` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `DomSupportedAdaption`
--
ALTER TABLE `DomSupportedAdaption`
  ADD CONSTRAINT `DomSupportedAdaption_ibfk_1` FOREIGN KEY (`FK_domainName`) REFERENCES `Domain` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `DomSupportedSwitch`
--
ALTER TABLE `DomSupportedSwitch`
  ADD CONSTRAINT `DomSupportedSwitch_ibfk_1` FOREIGN KEY (`FK_domainName`) REFERENCES `Domain` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints der Tabelle `DomSupportedBandwidth`
--
ALTER TABLE `DomSupportedBandwidth`
  ADD CONSTRAINT `DomSupportedBandwidth_ibfk_1` FOREIGN KEY (`FK_domainName`) REFERENCES `Domain` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;
