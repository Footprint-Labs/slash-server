SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS  `slash_server` ;

use slash_server;


-- -----------------------------------------------------
-- Table `slash_server`.`snippet_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`snippet_group` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`snippet_group` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `name` VARCHAR(45) NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`snippet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`snippet` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`snippet` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `url` VARCHAR(200) NOT NULL ,
  `name` VARCHAR(200) NOT NULL ,
  `snippet_group_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_snippet_group` (`snippet_group_id` ASC) ,
  CONSTRAINT `fk_snippet_group`
    FOREIGN KEY (`snippet_group_id` )
    REFERENCES `slash_server`.`snippet_group` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`role` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`role` (
  `id` INT NOT NULL  AUTO_INCREMENT  ,
  `name` VARCHAR(200) NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`page_template`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`page_template` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`page_template` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `name` VARCHAR(45) NULL ,
  `content` VARCHAR(20000),
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`page`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`page` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`page` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `url` VARCHAR(200) NOT NULL ,
  `name` VARCHAR(200) NOT NULL ,
  `markup` varchar(20000),
  `role_id` INT NULL ,
  `page_template_id` INT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `url_UNIQUE` (`url` ASC) ,
  INDEX `fk_page_role` (`role_id` ASC) ,
  INDEX `fk_page_template` (`page_template_id` ASC) ,
  CONSTRAINT `fk_page_role`
    FOREIGN KEY (`role_id` )
    REFERENCES `slash_server`.`role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_page_template`
    FOREIGN KEY (`page_template_id` )
    REFERENCES `slash_server`.`page_template` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`page_snippet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`page_snippet` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`page_snippet` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `snippet_id` INT NOT NULL ,
  `uuid` varchar(200) not null,
  `page_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `snippet_fk` (`snippet_id` ASC) ,
  INDEX `page_fk` (`page_id` ASC) ,
  CONSTRAINT `snippet_fk`
    FOREIGN KEY (`snippet_id` )
    REFERENCES `slash_server`.`snippet` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `page_fk`
    FOREIGN KEY (`page_id` )
    REFERENCES `slash_server`.`page` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`user` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`user` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `name` VARCHAR(200),
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
COMMENT = 'note not unique index on snippet_id,page_id as could have sa' /* comment truncated */ ;


-- -----------------------------------------------------
-- Table `slash_server`.`user_data`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`user_data` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`user_data` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `user_id` INT NOT NULL ,
  `page_snippet_id` INT NOT NULL ,
  `name` VARCHAR(200) NOT NULL ,
  `value` VARCHAR(500) NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `user_fk` (`user_id` ASC) ,
  INDEX `page_snippet_fk` (`page_snippet_id` ASC) ,
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id` )
    REFERENCES `slash_server`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `page_snippet_fk`
    FOREIGN KEY (`page_snippet_id` )
    REFERENCES `slash_server`.`page_snippet` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`sitemap`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`sitemap` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`sitemap` (
  `id` INT NOT NULL  AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`sitemap_page`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`sitemap_page` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`sitemap_page` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `sitemap_id` INT NOT NULL ,
  `index` int,
  `parent_id` INT NULL ,
  `page_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_sitemap_page_sitemap` (`sitemap_id` ASC) ,
  INDEX `fk_sitemap_page_parent` (`parent_id` ASC) ,
  INDEX `fk_sitemap_page_page` (`page_id` ASC) ,
  CONSTRAINT `fk_sitemap_page_sitemap`
    FOREIGN KEY (`sitemap_id` )
    REFERENCES `slash_server`.`sitemap` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sitemap_page_parent`
    FOREIGN KEY (`parent_id` )
    REFERENCES `slash_server`.`page` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sitemap_page_page`
    FOREIGN KEY (`page_id` )
    REFERENCES `slash_server`.`page` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `slash_server`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `slash_server`.`user_role` ;

CREATE  TABLE IF NOT EXISTS `slash_server`.`user_role` (
  `id` INT NOT NULL  AUTO_INCREMENT ,
  `user_id` INT NOT NULL ,
  `role_id` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  INDEX `fk_user_role_role` (`role_id` ASC) ,
  INDEX `fk_user_role_user` (`user_id` ASC) ,
  CONSTRAINT `fk_user_role_role`
    FOREIGN KEY (`role_id` )
    REFERENCES `slash_server`.`role` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_role_user`
    FOREIGN KEY (`user_id` )
    REFERENCES `slash_server`.`user` (`id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;




GRANT ALL ON slash_server.* TO slashserver_user@'%' IDENTIFIED BY 'slash';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

