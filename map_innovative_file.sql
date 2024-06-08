-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 28, 2020 at 05:11 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `map_innovative_file`
--

-- --------------------------------------------------------

--
-- Table structure for table `file_ware_house`
--

CREATE TABLE `file_ware_house` (
  `ID` int(11) NOT NULL,
  `Email` text NOT NULL,
  `Location` longtext NOT NULL,
  `Name` longtext NOT NULL,
  `Uq` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `file_ware_house`
--

INSERT INTO `file_ware_house` (`ID`, `Email`, `Location`, `Name`, `Uq`) VALUES
(27, 'meet0fatel@gmail.com', 'pdf', '1Chapter12-Regression-PolynomialRegression.pdf', '11Chapter12-Regression-PolynomialRegression.pdf'),
(33, 'meet0fatel@gmail.com', 'images', 'IMG-20201111-WA0006.jpg', '3IMG-20201111-WA0006.jpg'),
(34, 'meet0fatel@gmail.com', 'pdf', 'dharmik_laghumati_form.pdf', '4dharmik_laghumati_form.pdf'),
(35, 'meet0fatel@gmail.com', 'pdf', '1Chapter12-Regression-PolynomialRegression.pdf', '51Chapter12-Regression-PolynomialRegression.pdf');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `file_ware_house`
--
ALTER TABLE `file_ware_house`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `file_ware_house`
--
ALTER TABLE `file_ware_house`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
