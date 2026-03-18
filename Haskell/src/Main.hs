{-# LANGUAGE DataKinds #-}
{-# OPTIONS_GHC -Wno-unused-top-binds #-}
{-# OPTIONS_GHC -fplugin GHC.TypeLits.Normalise #-}

module Main where

import System.Environment (getArgs)
import qualified Data.Text as T
import qualified Data.Text.IO as T
import qualified Data.Text.Read as TR
import qualified Data.Vector as VB
import Linear.V
import Numeric.Natural (Natural)
import Serial 
import GHC.TypeLits (KnownNat)
import qualified Data.Vector as V
import GHC.TypeNats
import Data.Data (Proxy(..))

type V5 = V 5 Double

parseVs :: forall d. KnownNat d => T.Text -> Either String (V d Double)
parseVs t =
  let fields = T.splitOn (T.pack ",") t
      expectedDim = fromIntegral $ natVal (Proxy @d)
  in case traverse (TR.double . T.strip) fields of
      Right xs
        | length xs == expectedDim ->
            Right (V (VB.fromList (map fst xs)))
        | otherwise ->
            Left $ "Linha inválida: esperado " ++ show expectedDim ++ " colunas, mas recebi " ++ show (length xs)
      Left _ -> Left "Linha inválida: parse numérico falhou"

readCSVs :: forall d. KnownNat d => FilePath -> IO [V d Double]
readCSVs fp = do
  ls <- T.lines <$> T.readFile fp
  case traverse parseVs ls of
    Right ps -> pure ps
    Left e   -> fail e

main :: IO ()
main = do
  args <- getArgs
  case args of
    [kStr, dStr, max_itersStr, file] -> do
      let k :: Int
          k = read kStr
          dVal :: Natural
          dVal = read dStr
          max_iters = read max_itersStr

      case someNatVal dVal of
        SomeNat @d _ -> do
          points <- readCSVs @d file 
          print (kMeans k max_iters (V.fromList points))

    _ -> putStrLn "Use: cabal run k-means -- <k> <d> <max_iters> <arquivo.csv>>"
